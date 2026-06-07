# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

VideoSample is an Android app (Kotlin + Jetpack Compose) that displays a list of videos and plays
them — essentially a small YouTube-style sample app. It's a learning/sample project built up
feature-by-feature (see git log for the progression: domain layer → mock repository → Hilt DI →
video list UI → video list ViewModel → player ViewModel).

## Common commands

Run all commands from the project root using the Gradle wrapper.

```sh
./gradlew assembleDebug          # Build debug APK
./gradlew test                   # Run JVM unit tests (app/src/test)
./gradlew connectedAndroidTest   # Run instrumented tests on a connected device/emulator (app/src/androidTest)
./gradlew lint                   # Run Android Lint
```

To run a single JVM test class or method (standard Gradle test filtering):

```sh
./gradlew test --tests "com.example.videosample.ExampleUnitTest"
./gradlew test --tests "com.example.videosample.ExampleUnitTest.addition_isCorrect"
```

There is no separate root-level test task — `:app` is the only module (`settings.gradle.kts`).

## Architecture

The app follows a layered architecture (domain / data / di / ui), all under
`com.example.videosample`:

- **`domain.model`** — Plain data classes shared across layers, e.g. `Video`.
- **`domain.repository`** — Repository *interfaces* (e.g. `VideoRepository`) that the UI/ViewModel
  layer depends on. These define the contract (`getVideos`, `getVideo(id)`, `getRelatedVideos(id)`)
  independent of any data source implementation.
- **`data.repository`** — Concrete repository implementations. Currently only
  `MockVideoRepositoryImpl`, which generates fixed placeholder `Video` data and simulates network
  latency with `delay(...)` — there is no real backend yet. When a real implementation is added,
  it should implement `VideoRepository` the same way and be swapped in via the DI module.
- **`di`** — Hilt modules. `RepositoryModule` binds `VideoRepository` to its current implementation
  (`MockVideoRepositoryImpl`) as a `@Singleton` using `@Binds`. To switch implementations, change
  the binding here rather than touching ViewModels/UI.
- **`ui.<feature>`** — One package per screen/feature (e.g. `ui.videolist`, `ui.videoplayer`),
  each typically containing:
  - A `@HiltViewModel` (e.g. `VideoListViewModel`, `VideoPlayerViewModel`) that exposes a single
    `StateFlow<XxxUiState>` built with `MutableStateFlow`/`asStateFlow()` and loads data via
    `viewModelScope.launch`.
  - A `sealed interface XxxUiState` with `Loading` / `Success` / `Error` states (see
    `VideoListUiState`, or the inline state in `VideoPlayerViewModel`).
  - A `@Composable` screen function that takes the ViewModel, collects `uiState` with
    `collectAsState()`, and renders based on a `when` over the sealed state.
  - A `components` sub-package for reusable Composables specific to that feature (e.g.
    `ui.videolist.components.VideoCard`, which has a `@Preview` Composable for design-time preview).
- **`ui.theme`** — Standard Compose Material3 theme setup (`Theme.kt`, `Color.kt`, `Type.kt`).

Entry points: `VideoApplication` (`@HiltAndroidApp`) and `MainActivity` (`@AndroidEntryPoint`),
which obtains `VideoListViewModel` via `by viewModels()` and sets the Compose content with
`VideoSampleTheme { Scaffold { VideoListScreen(...) } }`.

## Conventions to follow

- KDoc/comments in this codebase are written in **Japanese** — match this style for new code in
  existing files (e.g. `VideoRepository`, `MockVideoRepositoryImpl`).
- Dependency injection is via **Hilt**: annotate ViewModels with `@HiltViewModel` and constructor-
  inject dependencies with `@Inject`; bind interface→impl pairs in a `@Module`/`@InstallIn` class
  under `di`, using `@Binds` + `@Singleton` for repositories.
- UI state is modeled as a `sealed interface` with `Loading`/`Success`/`Error` variants and exposed
  as a single `StateFlow` from the ViewModel — follow this pattern for any new screen rather than
  exposing multiple independent flows.
- Images are loaded with **Coil 3** (`coil3.compose.AsyncImage`); thumbnail URLs in the mock
  repository point to `picsum.photos` placeholders, and the test video URL is an HLS (`.m3u8`)
  stream from `test-streams.mux.dev`.
- `MockVideoRepositoryImpl` is the only data source right now (bound in `RepositoryModule`) and is
  explicitly intended to be temporary — expect a real network-backed implementation to replace it
  later.
