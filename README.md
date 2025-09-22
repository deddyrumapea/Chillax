# Chillax

<a href="https://play.google.com/store/apps/details?id=com.romnan.chillax" target="_blank">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="100"/>
</a>

## Overview
Chillax is a free relaxation and sleep companion for Android that lets you blend high-quality ambient loops into your own calming environment. The app is intentionally distraction-free—no ads, upsells, or sign-ups—so you can focus on unwinding, improving sleep hygiene, or creating a productive background soundscape.

## Table of contents
- [Overview](#overview)
- [Features](#features)
- [Tech stack](#tech-stack)
- [Architecture](#architecture)
- [Getting started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Run the app](#run-the-app)
  - [Run tests](#run-tests)
- [Project structure](#project-structure)
- [Assets & credits](#assets--credits)
- [Support](#support)
- [Privacy](#privacy)

## Features
- Curated catalogue of nature, weather, household, and travel ambiences ready to mix and match.
- Layer multiple sounds with individual volume sliders to craft personal mixes for focus, meditation, or sleep.
- Save custom mixes with cover art for quick access alongside the included presets.
- Sleep timer that fades everything out at a chosen time so playback stops automatically.
- Optional bedtime reminders (with notification permission) to build a consistent wind-down routine.
- Background playback with a persistent notification, making it easy to control sounds while using other apps or with the screen off.
- Works fully offline thanks to locally bundled audio loops—perfect for flights and remote getaways.
- Simplified Chinese localisation in addition to English.

## Tech stack
- **Language:** Kotlin with Kotlin Coroutines and Flow.
- **UI:** Jetpack Compose + Material 3, including custom components and Compose Destinations for navigation.
- **Architecture:** MVVM with lifecycle-aware `ViewModel`s backed by repository interfaces.
- **Dependency injection:** Dagger Hilt.
- **Data & storage:** Jetpack DataStore (with Kotlin Serialization) for user preferences, custom mixes, and player state.
- **Media playback:** AndroidX Media3 (ExoPlayer) for smooth, gapless audio mixing.
- **Async & utilities:** Kotlinx Collections Immutable, Coil for image loading, Accompanist System UI Controller, and Compose NumberPicker.
- **Firebase integrations:** Remote Config, Analytics, and Crashlytics for configuration and quality monitoring (optional for contributors).

## Architecture
The project follows a single-module clean architecture inspired layout:

- `data` – Local data sources, repositories, and serializers that persist app settings, custom mixes, and timer state.
- `domain` – Immutable models plus repository interfaces that describe business logic without Android dependencies.
- `presentation` – Jetpack Compose UI, state holders, and `ViewModel`s that react to Flows exposed by the domain layer.
- `di` – Hilt modules wiring repositories, system services, and configuration objects together.
- `application` – Application-level setup including Hilt entry points and Firebase initialisation hooks.

This separation keeps UI code declarative while ensuring the player, sleep timer, and reminder logic remain testable and reusable.

## Getting started

### Prerequisites
- Android Studio Koala (2024.1.2) or newer with the Android Gradle Plugin 8.12.0.
- JDK 17 (bundled with modern Android Studio releases).
- Android SDK Platform 35 and build tools installed.
- A device or emulator running Android 5.0 (API 21) or later.

### Run the app
1. Clone the repository: `git clone https://github.com/romnan/Chillax.git` and open it in Android Studio.
2. Allow Gradle to sync dependencies. The included `google-services.json` enables Firebase features out of the box.
3. Choose a deployment target (emulator or USB device) and click **Run** ▶️, or build from the command line with `./gradlew assembleDebug`.
4. Install the debug build on a device using `./gradlew installDebug` if you prefer the terminal workflow.

### Run tests
- Unit tests: `./gradlew test`
- Instrumented UI tests (device/emulator required): `./gradlew connectedAndroidTest`

## Project structure
```
app/
├── src/main/java/com/romnan/chillax
│   ├── application/        # Application class and startup helpers
│   ├── data/               # DataStore serializers, repositories, and data sources
│   ├── di/                 # Hilt dependency injection modules
│   ├── domain/             # Domain models and repository contracts
│   └── presentation/       # Compose UI, state models, and ViewModel logic
├── src/main/res
│   ├── raw/                # Built-in ambience loops and preset artwork (offline playback)
│   └── values-zh-rCN/      # Simplified Chinese localisation resources
├── build.gradle            # Module configuration and dependency graph
└── ...
```

## Assets & credits
Attribution for icons and audio loops lives in [`ATTRIBUTIONS.md`](ATTRIBUTIONS.md). Please review it before reusing assets outside the app.

## Support
Questions, ideas, or bug reports? Reach out via email at [romnanstudio@gmail.com](mailto:romnanstudio@gmail.com). User feedback directly shapes upcoming improvements, so don't hesitate to get in touch.

## Privacy
The project ships with a [privacy policy](PRIVACY_POLICY.md) that mirrors the Google Play Store listing. Firebase services are optional; disable or replace the configuration files if you need a build without analytics.
