# Task checklist

Mobile Android client (progress):

- [x] Inspect web client API endpoints and extract paths
- [x] Add Retrofit interfaces and client wiring
- [x] Create Android data models (POJOs)
- [x] Implement XML layouts and Activities for: Login, Register, Change Password, Dashboard
- [x] Wire network calls, token storage (SharedPreferences), and navigation
- [x] Fix startup crash: switch app theme to MaterialComponents (AppCompat-compatible)
- [ ] Test build and basic flows on emulator/device (user will run)
- [ ] Improve error handling and UI validation
- [ ] Update Android README with advanced notes and troubleshooting

Server (notes):

- Backend base URL used by web: `http://localhost:8080`
- Android emulator uses `http://10.0.2.2:8080` to reach the host machine

Files added/changed (mobile client):

- `app/build.gradle.kts` — added Retrofit/OkHttp and appcompat/material dependencies
- `app/src/main/java/.../api/` — `ApiService.kt`, `RetrofitClient.kt`, `models/AuthModels.kt`
- `app/src/main/java/.../LoginActivity.kt`, `RegisterActivity.kt`, `ChangePasswordActivity.kt`, `DashboardActivity.kt`
- `app/src/main/res/layout/` — `activity_login.xml`, `activity_register.xml`, `activity_change_password.xml`, `activity_dashboard.xml`
- `app/src/main/AndroidManifest.xml` — launcher changed to `LoginActivity`, added activities, added `INTERNET` permission
- `app/src/main/res/values/themes.xml` — switched to `Theme.MaterialComponents.DayNight.NoActionBar` to fix startup crash

Notes:

- I intentionally left `MainActivity.kt` (Compose) in place; `LoginActivity` is the launcher and uses XML layouts.
- I did not run Gradle builds; please run the build and share Logcat output for any further crashes.
