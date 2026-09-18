
YE 10" ANDROID RADIO KE LIYE HD LAUNCHER PROJECT HAI
=========================================

FEATURES:
- Speed: GPS se real km/h (81 jaise)
- Watch: 08:23 Real time + TUESDAY
- Speed Meter: Left/Right rainbow bars speed ke sath badhte hain
- Pistons: 4 pistons ka animation speed ke hisab se tez hota hai
- Road View: Center me road moving animation car ke sath
- Compass: Real sensor se North direction
- Trip Timer: 02:30 jaise timer

LAPTOP SE APK KAISE BANAYE - STEP BY STEP:
-----------------------------------------
1. Android Studio Install karo (https://developer.android.com/studio)
2. Android Studio open karo -> Open -> is folder CarDashboardLauncher ko select karo
3. Gradle Sync hone do (internet chahiye pehli bar)
4. File me jo HD image hai vo app/src/main/res/drawable me hai. Apna image badalna ho to replace kar do same naam se.
5. Top menu me Build -> Make Project (Ctrl+F9) - error check karo
6. Apna 10 inch radio ko USB debugging ON karke laptop se connect karo ya fir APK build karo:
   Build -> Build Bundle(s) / APK(s) -> Build APK(s)
7. APK yahan milega: app/build/outputs/apk/debug/app-debug.apk
8. Us APK ko USB stick me dalke car radio me install karo. Launcher permission maangega to ALLOW karo aur Default Launcher set karo isko.
9. Agar weather API lagana hai to DashboardLauncherActivity.kt me openweathermap key dal sakte ho.

GPS NOTE:
- Car me GPS antenna connected hona chahiye warna speed 0 rahegi. Testing ke liye mobile hotspot + GPS use karo.
- Android 6+ me location permission allow karna zaruri hai.

10 INCH OPTIMIZE:
- AndroidManifest me screenOrientation landscape rakha hai, full screen theme hai.
- Layout 1280x800 aur 1024x600 dono pe responsive hai.

Koi error aaye to mujhe batao!
