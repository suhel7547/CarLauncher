
package com.carlauncher.hd

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class DashboardLauncherActivity : AppCompatActivity(), LocationListener, SensorEventListener {

    private lateinit var tvSpeed: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvDay: TextView
    private lateinit var tvTrip: TextView
    private lateinit var tvWeather: TextView
    private lateinit var tvWeatherDesc: TextView
    private lateinit var tvCompassTimer: TextView
    private lateinit var roadView: RoadView
    private lateinit var pistonView: PistonView
    private lateinit var rpmLeft: RpmBarView
    private lateinit var rpmRight: RpmBarView
    private lateinit var compassView: CompassView

    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private var currentSpeedKmh = 0f
    private var tripSeconds = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    // TODO: Yahan apna OpenWeather API Key dalo - free me milta hai
    private val OPEN_WEATHER_API_KEY = "YOUR_API_KEY_HERE"
    private var lastWeatherFetch = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tvSpeed = findViewById(R.id.tvSpeed)
        tvTime = findViewById(R.id.tvTime)
        tvDay = findViewById(R.id.tvDay)
        tvTrip = findViewById(R.id.tvTrip)
        tvWeather = findViewById(R.id.tvWeather)
        tvWeatherDesc = findViewById(R.id.tvWeatherDesc)
        tvCompassTimer = findViewById(R.id.tvCompassTimer)
        roadView = findViewById(R.id.roadView)
        pistonView = findViewById(R.id.pistonView)
        rpmLeft = findViewById(R.id.rpmLeft)
        rpmRight = findViewById(R.id.rpmRight)
        compassView = findViewById(R.id.compassView)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        requestPermissions()
        startClock()
        startTripTimer()
        startSensors()
    }

    private fun requestPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        } else { startGPS() }
    }

    override fun onRequestPermissionsResult(code:Int, perms:Array<String>, res:IntArray){
        super.onRequestPermissionsResult(code,perms,res)
        if(code==101) startGPS()
    }

    private fun startGPS(){
        try{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0f, this)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
            }
        }catch(e:Exception){ e.printStackTrace() }
    }

    private fun startClock(){
        handler.post(object: Runnable{
            override fun run(){
                val now = Date()
                tvTime.text = timeFormat.format(now)
                tvDay.text = dayFormat.format(now).uppercase()
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun startTripTimer(){
        handler.post(object: Runnable{
            override fun run(){
                tripSeconds++
                val m = (tripSeconds%3600)/60
                val s = tripSeconds%60
                tvTrip.text = String.format("%02d:%02d", m, s)
                tvCompassTimer.text = String.format("00:%02d", s)
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun startSensors(){
        val rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        rotation?.let{ sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onLocationChanged(loc: Location){
        if(loc.hasSpeed()){
            currentSpeedKmh = loc.speed * 3.6f
        }
        tvSpeed.text = currentSpeedKmh.toInt().toString()
        roadView.setSpeed(currentSpeedKmh)
        pistonView.setSpeed(currentSpeedKmh)
        rpmLeft.setLevel((currentSpeedKmh/2).coerceIn(0f,100f))
        rpmRight.setLevel((currentSpeedKmh/2).coerceIn(0f,100f))

        // Weather update - har 10 min me ek bar
        val now = System.currentTimeMillis()
        if(now - lastWeatherFetch > 10*60*1000){
            fetchLiveWeather(loc.latitude, loc.longitude)
            lastWeatherFetch = now
        }
    }

    private fun fetchLiveWeather(lat: Double, lon: Double){
        if(OPEN_WEATHER_API_KEY == "YOUR_API_KEY_HERE"){
            // Agar key nahi dali to demo value dikhao
            handler.post{
                tvWeather.text = "10°"
                tvWeatherDesc.text = "light rain\nRelax (Add API Key for live)"
            }
            return
        }
        thread{
            try{
                val urlStr = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$OPEN_WEATHER_API_KEY"
                val jsonStr = URL(urlStr).readText()
                val json = JSONObject(jsonStr)
                val temp = json.getJSONObject("main").getDouble("temp").toInt()
                val desc = json.getJSONArray("weather").getJSONObject(0).getString("description")
                val icon = json.getJSONArray("weather").getJSONObject(0).getString("main")

                handler.post{
                    tvWeather.text = "${temp}°"
                    tvWeatherDesc.text = "${desc}\n${icon}"
                }
            }catch(e:Exception){
                e.printStackTrace()
                handler.post{
                    tvWeather.text = "10°"
                    tvWeatherDesc.text = "offline"
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?){
        if(event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR){
            val rotMat = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotMat, event.values)
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotMat, orientation)
            val azimuthDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()
            compassView.setAzimuth(azimuthDeg)
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, acc: Int){}
    override fun onDestroy(){ super.onDestroy(); locationManager.removeUpdates(this); sensorManager.unregisterListener(this) }
}
