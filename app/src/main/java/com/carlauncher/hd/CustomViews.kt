
package com.carlauncher.hd

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class RoadView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet?=null): View(ctx, attrs){
    private var speed=0f
    private var offset=0f
    private val paintRoad = Paint().apply{ color=Color.rgb(20,20,20); style=Paint.Style.FILL }
    private val paintLine = Paint().apply{ color=Color.WHITE; strokeWidth=6f; style=Paint.Style.STROKE }
    private val paintDashed = Paint().apply{ color=Color.WHITE; strokeWidth=4f; style=Paint.Style.STROKE; pathEffect= DashPathEffect(floatArrayOf(20f,20f),0f) }

    fun setSpeed(s: Float){ speed=s; invalidate() }

    override fun onDraw(c: Canvas){
        super.onDraw(c)
        val w=width.toFloat(); val h=height.toFloat()
        // road perspective
        val roadPath = Path().apply{
            moveTo(w*0.35f, h); lineTo(w*0.48f, 0f); lineTo(w*0.52f,0f); lineTo(w*0.65f,h); close()
        }
        c.drawPath(roadPath, paintRoad)
        // center dashed
        offset = (offset + speed*0.2f) % 40
        paintDashed.pathEffect = DashPathEffect(floatArrayOf(20f,20f), offset)
        c.drawLine(w*0.5f,0f,w*0.5f,h,paintDashed)
        c.drawLine(w*0.42f,0f,w*0.28f,h,paintLine)
        c.drawLine(w*0.58f,0f,w*0.72f,h,paintLine)
        // simple car shadow at center
        val carPaint = Paint().apply{ color=Color.BLACK; alpha=200 }
        c.drawOval(w*0.47f, h*0.65f, w*0.53f, h*0.75f, carPaint)
    }
}

class PistonView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet?=null): View(ctx, attrs){
    private var speed=0f
    private var phase=0f
    private val paint = Paint().apply{ color=Color.CYAN; style=Paint.Style.STROKE; strokeWidth=3f }
    private val fill = Paint().apply{ color=Color.argb(80,0,255,255); style=Paint.Style.FILL }

    fun setSpeed(s:Float){ speed=s; postInvalidateOnAnimation() }

    override fun onDraw(c: Canvas){
        phase+= 0.1f + speed*0.01f
        val w=width; val h=height
        // draw 4 pistons moving up-down with sin
        for(i in 0..3){
            val cx = w* (0.2f + i*0.2f)
            val baseY = h*0.5f + sin(phase + i*1.2f)* h*0.15f
            // cylinder
            c.drawRoundRect(cx-30, baseY-60, cx+30, baseY+20, 15f,15f, paint)
            c.drawRoundRect(cx-30, baseY-60, cx+30, baseY+20, 15f,15f, fill)
            // rod
            c.drawLine(cx, baseY+20, cx, baseY+80, paint)
            c.drawCircle(cx, baseY+90, 15f, paint)
        }
        if(speed>0 || true) postInvalidateDelayed(16)
    }
}

class RpmBarView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet?=null): View(ctx, attrs){
    private var level=0f
    private val colors = intArrayOf(Color.RED, Color.parseColor("#FF8800"), Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE)
    fun setLevel(l: Float){ level=l; invalidate() }
    override fun onDraw(c: Canvas){
        val bh = height/20f
        val bars = (level/5).toInt().coerceIn(0,20)
        for(i in 0 until 20){
            val colorIdx = (i*colors.size/20)
            val p = Paint().apply{ color=colors[colorIdx]; style=Paint.Style.FILL; alpha= if(i<bars)255 else 60 }
            val top = height - (i+1)*bh
            c.drawRect(0f, top, width.toFloat(), top+bh*0.8f, p)
        }
    }
}

class CompassView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet?=null): View(ctx, attrs){
    private var azimuth=0f
    private val paint = Paint().apply{ color=Color.WHITE; style=Paint.Style.STROKE; strokeWidth=2f; isAntiAlias=true }
    fun setAzimuth(a: Float){ azimuth=a; invalidate() }
    override fun onDraw(c: Canvas){
        val cx=width/2f; val cy=height/2f; val r=min(width,height)/2f*0.9f
        c.drawCircle(cx,cy,r,paint)
        c.save(); c.rotate(-azimuth, cx,cy)
        val p = Path().apply{ moveTo(cx,cy-r*0.6f); lineTo(cx-10,cy); lineTo(cx+10,cy); close() }
        c.drawPath(p, Paint().apply{ color=Color.WHITE; style=Paint.Style.FILL })
        c.restore()
    }
}
