# Project Plan

Easy Sartorial: A mobile outfit advisor for classic menswear. Features include Outfit Builder, Random Outfit Generator, Pattern Education Library, Outfit Scoring, and Accessory Coordination. Built with Kotlin Multiplatform, Jetpack Compose, and Supabase.

## Project Brief

# Easy Sartorial Project Brief

**Easy Sartorial** is a mobile advisor designed for classic menswear enthusiasts. It bridges the gap between traditional tailoring rules and modern technology, helping users build sophisticated outfits with confidence.

## Features
*   **Outfit Builder**: An interactive canvas to visually assemble and preview combinations of suits, shirts, ties, and footwear.
*   **Random Outfit Generator**: An algorithm-driven tool that suggests classically coordinated outfits based on color theory and formal wear standards.
*   **Pattern Education Library**: A comprehensive reference guide for identifying classic patterns (e.g., Glen Check, Houndstooth, Birdseye) with advice on how to mix them.
*   **Accessory Coordination**: A smart recommendation system for pairing pocket squares, watches, and shoes based on the selected primary garments.

## High-Level Technical Stack
*   **Kotlin**: The core language for modern, concise, and safe Android development.
*   **Jetpack Compose**: A declarative UI toolkit using **Material 3** to create a vibrant and energetic user experience with full edge-to-edge support.
*   **Kotlin Coroutines & Flow**: For managing asynchronous data streams and background tasks.
*   **KSP (Kotlin Symbol Processing)**: Utilized for high-performance code generation for networking and data mapping.
*   **Retrofit & Moshi**: To handle communication with the Supabase backend and parse sartorial data.
*   **Coil**: An image-loading library for high-quality rendering of garment textures and patterns.
*   **Compose Navigation**: To manage the app's flow through a single-activity architecture.

## Implementation Steps

### Task_1_Foundation_and_Library: Define sartorial data models (Garment, Pattern, Color) and implement the Pattern Education Library screen with a basic navigation structure.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Data models for Garments, Patterns, and Outfits are implemented.
  - Pattern Education Library screen displays patterns and coordination advice.
  - Basic Navigation (Compose Navigation) is functional.
  - Project builds successfully.
- **StartTime:** 2026-03-21 21:50:38 ICT

### Task_2_Outfit_Builder_and_Generator: Implement the interactive Outfit Builder canvas and the Random Outfit Generator algorithm with Accessory Coordination logic.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Users can visually assemble outfits in the Builder.
  - Random Outfit Generator provides suggestions based on color theory.
  - Accessory Coordination system suggests matching pocket squares and shoes.
  - App logic is stable and handles data correctly.

### Task_3_M3_Theming_and_Icon: Apply a vibrant Material 3 theme, implement full Edge-to-Edge display, and create an adaptive app icon.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Material 3 theme with light/dark modes and vibrant colors is applied.
  - Full Edge-to-Edge display is implemented across all screens.
  - Adaptive app icon is created and functional.
  - UI follows Material Design 3 aesthetic guidelines.

### Task_4_Run_and_Verify: Perform a final run of the application to verify stability, feature completeness, and adherence to the project brief.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Application is stable and does not crash.
  - All existing tests pass (if any).
  - Build passes successfully.
  - All core features (Builder, Generator, Library) meet the brief requirements.

