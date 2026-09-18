
=== WEATHER LIVE KAISE ADD KARE ===
1. https://openweathermap.org/ pe free account banao
2. API keys section me jao, ek key copy karo (free me 1000 calls/day milta hai)
3. Android Studio me DashboardLauncherActivity.kt open karo
4. Line: private val OPEN_WEATHER_API_KEY = "YOUR_API_KEY_HERE"
   Yahan apni key paste karo
5. Fir Build > Build APK - ab live weather ayega!

Weather automatically GPS location se ayega. Har 10 min me update hoga.

=== LAPTOP NAHI HAI? ONLINE APK KAISE BANAYE (100% FREE) ===

METHOD 1 - GITHUB SE (Sabse easy, bina Android Studio ke):

1. github.com pe account banao / login karo
2. New Repository banao naam: CarLauncher
3. Is zip ka saara content upload karo (drag & drop)
4. Repository me upar Actions tab pe click karo
5. Left me "Build APK Online" dikhega > Run workflow > Run
6. 3-4 min me build hoga. Neeche Artifacts me "CarHDLauncher-APK" me se app-debug.apk download karo
7. Direct radio me install!

METHOD 2 - APKC Online Builder:

1. https://www.codeassist.app ya https://appetize.io ya https://replit.com pe jao
2. New Android Project banao, is project ke files paste karo
3. Build button se APK download karo

METHOD 3 - AndroidIDE app (Mobile se):

1. Play Store se "AndroidIDE" app install karo (phone me)
2. Is zip ko phone me extract karo
3. AndroidIDE me project open karo > Build APK
4. Phone se hi APK ban jayega, fir USB se radio me dalo

TIPS FOR 10" RADIO:
- Resolution 1280x720 ke liye optimized hai
- Android 6.0+ pe chalega (Android 10,11,12,13 sab support)
- Launcher permission allow karna zaruri hai
- GPS ke bina speed 0 rahegi, testing ke liye mobile GPS use karo
