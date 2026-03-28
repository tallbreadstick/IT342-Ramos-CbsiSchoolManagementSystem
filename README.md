# CBSI School Management System

This repository contains the CBSI backend (Spring), a React web client, and an Android client.

Quick Android client notes
- The Android client implements the same auth flows as the web: `POST /api/auth/login`, `POST /api/auth/register`, and `POST /api/auth/change-password`.
- Web base URL: `http://localhost:8080`. For the Android emulator use `http://10.0.2.2:8080` (handled by the Retrofit client).
- The Android app ships XML-based Activities for Login, Register, Change Password, and a placeholder Dashboard. `LoginActivity` is the launcher.

Where to look (mobile client)
- `mobile/CBSI-Android-Client/app/src/main/java/.../api/` — Retrofit setup and models
- `mobile/CBSI-Android-Client/app/src/main/java/.../LoginActivity.kt` — login flow
- `mobile/CBSI-Android-Client/app/src/main/java/.../RegisterActivity.kt` — register flow
- `mobile/CBSI-Android-Client/app/src/main/java/.../ChangePasswordActivity.kt` — change-password flow
- `mobile/CBSI-Android-Client/app/src/main/res/layout/` — XML layouts for the screens

How to test the Android client (emulator)
1. Start the Spring backend locally (port 8080). The web client expects this.
2. Run the Android emulator from Android Studio.
3. Build and install the debug APK from the `mobile/CBSI-Android-Client` project.

Build commands (from workspace root):
```bash
cd mobile/CBSI-Android-Client
# Windows
.\gradlew.bat assembleDebug
# then install or run from Android Studio
```

Notes and troubleshooting
- If the app crashes at startup, ensure the theme in `app/src/main/res/values/themes.xml` inherits from a MaterialComponents/AppCompat theme. This repository sets `Theme.MaterialComponents.DayNight.NoActionBar`.
- If the app cannot reach the backend on an emulator, change `RetrofitClient.BASE_URL` to your machine IP or use `10.0.2.2` for emulator.
- Token storage: the app stores the JWT in `SharedPreferences` under `cbsi_token` and attaches it as `Authorization: Bearer <token>`.

If you want, I can update the README with more details (endpoint payloads, examples, or how to run the backend). 

