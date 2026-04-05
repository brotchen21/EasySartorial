# Easy Sartorial

An Android application designed to help users build and validate sartorial outfits using a rule-based scoring engine. The app provides visual feedback on color compatibility, pattern matching, and overall outfit harmony.

## Features

- **Outfit Builder:** Manually select garments (jacket, shirt, trousers, etc.) and get real-time scoring and feedback.
- **Random Generator:** Automatically generate outfits that adhere to sartorial rules.
- **Pattern Library:** Browse and explore different fabric patterns and their characteristics.
- **Scoring Engine:** A comprehensive rule-based system that evaluates:
    - Foundation (Jacket + Trousers) compatibility.
    - Shirt contrast.
    - Tie harmony.
    - Color balance (bold vs. neutral).
    - Temperature consistency (warm vs. cool).
    - Pattern-color interaction.
    - Accessory matching (shoes & belt).
- **Visual Representation:** Uses layering (base, shading, pattern overlays) to represent garments on a mannequin.

## Tech Stack

- **Language:** Kotlin
- **Framework:** Jetpack Compose (UI), Android Architecture Components (ViewModel, Navigation)
- **Networking:** Ktor, Retrofit, OkHttp
- **Backend/Database:** Supabase (Postgres, Storage, Functions)
- **Dependency Injection:** Manual DI (via `MainActivity`)
- **JSON Parsing:** Moshi
- **Image Loading:** Coil
- **Local Data:** Room, DataStore

## Requirements

- **Android Studio:** Ladybug (or newer recommended)
- **JDK:** 11 (as per `build.gradle.kts` configuration)
- **Android Device/Emulator:** API 28 (Android 9.0) or higher
- **Supabase Account:** Required for remote data and storage.

## Setup & Run

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Environment Variables:**
    Create a `local.properties` file in the project root if it doesn't exist. Add your Supabase credentials:
    ```properties
    SUPABASE_URL=your_supabase_project_url
    SUPABASE_ANON_KEY=your_supabase_anon_key
    ```
3.  **Build the project:**
    Open the project in Android Studio and let Gradle sync.
4.  **Run the app:**
    Select your device/emulator and click the **Run** button (Shift+F10).

## Scripts & Commands

- **Build Project:** `./gradlew assembleDebug`
- **Run Unit Tests:** `./gradlew test`
- **Run Instrumented Tests:** `./gradlew connectedAndroidTest`
- **Lint Check:** `./gradlew lint`

## Project Structure

```text
app/src/main/java/com/brotchen21/easysatorial/
├── core/
│   └── scoring/          # Scoring engine and sartorial logic
├── data/
│   ├── mapper/           # DTO to Domain model mappers
│   ├── remote/           # Supabase client and networking
│   └── repository/       # Repository implementations
├── domain/
│   ├── model/            # Business models (Garment, Outfit, etc.)
│   ├── repository/       # Repository interfaces
│   └── usecase/          # Business logic use cases
├── presentation/
│   ├── components/       # Reusable UI components
│   ├── navigation/       # Navigation definitions
│   ├── screens/          # Feature screens (Home, Builder, etc.)
│   └── viewmodels/       # ViewModels for the screens
└── ui/theme/             # Compose theme, colors, and typography
```

## TODOs

- [ ] Implement robust Dependency Injection (e.g., Hilt/Dagger).
- [ ] Add more comprehensive unit tests for the `ScoringEngine`.
- [ ] Implement user accounts and saved outfits.
- [ ] Enhance the mannequin visualization with more garment types.
- [ ] Setup CI/CD pipeline for automated testing and builds.

## License

TODO: Add license information (e.g., MIT, Apache 2.0).
