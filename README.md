# 📱 Iftikhar Khan - Android Developer Portfolio

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android Gradle Plugin](https://img.shields.io/badge/AGP-9.5-3DDC84.svg?style=flat&logo=android)](https://developer.android.com/studio/releases/gradle-plugin)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-34A853.svg?style=flat&logo=android)](https://developer.android.com)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-007ACC.svg?style=flat&logo=android)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A modern, native Android portfolio application built with **Kotlin**, **Material 3 Design**, **ViewBinding**, and **AndroidX Jetpack libraries**. Showcase your mobile developer profile, technical skills, published Google Play Store applications, embedded interactive web portfolio viewer, and social developer links.

---

## ✨ Features

- 👤 **Interactive Hero Profile Card:** Personal avatar, engineer title, availability badge, and quick bio.
- 📊 **Key Metrics & Statistics:** Highlight years of experience, published apps count, and core technologies.
- 🚀 **Featured Projects Showcase:** Dynamically populated project cards with category chip badges and direct links to live Google Play Store listings.
- 🌐 **Embedded In-App Web Viewer:** Dedicated `WebsiteActivity` with custom WebView configuration, progress loading bar, error state handling, and floating navigation dock (Back, Forward, Refresh, Share).
- 🎨 **Material 3 Design & Themes:** Clean color palette, proper `sp`/`dp` dimension scaling, custom splash screen (`androidx.core.splashscreen`), and light/dark theme support.
- 📱 **Edge-to-Edge Experience:** System bar insets integration via `WindowInsetsControllerCompat` and `ViewCompat`.

---

## 🛠️ Tech Stack & Architecture

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI & Layouts:** XML Layouts, Material Design 3 Components (`MaterialCardView`, `MaterialButton`, `MaterialToolbar`)
- **Architecture & View Binding:** Android ViewBinding, Repository Pattern (`PortfolioRepository`)
- **AndroidX & Jetpack:**
  - `androidx.core:core-ktx`
  - `androidx.appcompat:appcompat`
  - `androidx.constraintlayout:constraintlayout`
  - `androidx.core:core-splashscreen`
- **Testing:** JUnit 4 Unit Tests (`PortfolioRepositoryTest`)

---

## 📂 Project Structure

```
com.iftikhar.khan
├── MainActivity.kt               # Main portfolio screen & project listing
├── SplashActivity.kt             # Animated splash screen with smooth transition
├── WebsiteActivity.kt            # Embedded web browser viewer with floating dock
└── PortfolioRepository.kt        # Portfolio data models & repository
```

---

## 🚀 Getting Started & Building

1. **Clone the repository:**
   ```bash
   git clone https://github.com/iftikharkhan-android-app-developer/IftikharKhan.git
   cd IftikharKhan
   ```

2. **Open in Android Studio:**
   - Open Android Studio (Ladybug / 2024.2+ or latest).
   - Allow Gradle sync to download dependencies.

3. **Build & Run:**
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📄 License

```
MIT License

Copyright (c) 2025 Iftikhar Khan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR OTHER DEALINGS IN THE SOFTWARE.
```
