# 📱 GPS Emulator – Fake Location App (Kotlin)

**Version:** 1.0  
**Platform:** Android  
**Language:** Kotlin (Jetpack Compose)  
**Category:** Tools / Navigation  
**Target SDK:** 34+

---

## 🧭 Overview

**GPS Emulator** is a free and user-friendly Android app that lets users simulate (mock) any GPS location on their device.  
It's designed for developers, testers, and privacy-conscious users who want to:

- Test GPS-based applications  
- Simulate travel or route scenarios  
- Conceal real location for privacy  
- Preview geo-restricted content
- Route simulation with custom speed
- Dark mode support
- Google Places Autocomplete
- Export/import favorite locations
- Cloud sync support

When set as the *Mock Location App* in Developer Options, the app can make **all other apps** (WhatsApp, Instagram, Snapchat, etc.) detect the fake location you select.

## ✨ Features

- 🌍 **Fake GPS Location:** Set any latitude/longitude or search by name  
- 🗺️ **Map Picker:** Choose a location by dragging a pin on Google Map  
- 🔎 **Search Bar:** Find any location via Google Places API  
- 💾 **Save Favorites:** Store commonly used mock locations  
- ▶️ **Start/Stop Simulation:** Simple toggle for mocking  
- 🚶 **Simulate Movement:** Route playback (optional)  
- 🔔 **Foreground Service:** Keeps fake location active in background  
- 🕵️ **Privacy Mode:** Protect your real GPS info
- 🌙 **Dark Mode:** Comfortable viewing in low light
- ☁️ **Cloud Sync:** Access your saved locations across devices

## ⚙️ Technical Details

| Component | Description |
|------------|-------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose |
| **Architecture** | MVVM |
| **Map Service** | Google Maps SDK |
| **Background Task** | Foreground Service + Coroutines |
| **Dependency Injection** | Hilt |
| **Database** | Room |
| **Networking** | Retrofit |

## 🚀 Getting Started

### Prerequisites
- Android device with Developer Options enabled
- Android 8.0 (API level 26) or higher
- Google Play Services

### Installation
1. Download and install the APK
2. Enable Developer Options on your device
3. Set GPS Emulator as your Mock Location App
4. Grant necessary permissions
5. Start simulating locations!

## 🧠 How It Works

1. User enables Developer Options
2. Selects GPS Emulator as the Mock Location App
3. Picks a fake location on the map or searches by name
4. App pushes the selected location using LocationManager APIs
5. Other apps receive the fake GPS position

## 🏗️ Project Structure

```
com.example.gpsemulator/
├── data/
│   ├── local/         # Local database and preferences
│   ├── remote/        # API clients and data sources
│   └── repository/    # Data repositories
├── di/                # Dependency injection
├── domain/            # Business logic and use cases
├── service/           # Background services
├── ui/
│   ├── components/    # Reusable UI components
│   ├── map/           # Map screen and related components
│   ├── settings/      # Settings screen
│   └── theme/         # App theming
└── utils/             # Utility classes and extensions
```

## 🔐 Permissions

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

## 🎨 UI/UX

- Clean, modern interface with Material Design 3
- Interactive map with smooth animations
- Intuitive controls for location selection
- Dark/light theme support
- Responsive layout for different screen sizes

## ⚖️ Privacy & Legal

- No personal data is collected or shared
- Works only with explicit user consent
- Complies with Android's mock location framework
- Intended for testing, development, and privacy use
- Not for illegal or deceptive activities

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
