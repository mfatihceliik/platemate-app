# PlateMate

PlateMate is a native Android application built with Kotlin and Jetpack Compose. The app helps users search vehicle plates, view community reputation signals, leave reviews, discover trending plate activity, save plates, and use authentication-backed social and messaging workflows.

The project is structured as a portfolio-grade Android codebase with Clean Architecture, MVVM, repository/use-case boundaries, local persistence, REST integration, real-time socket infrastructure, and focused test coverage.

## Highlights

- Native Android app written in Kotlin
- Modern declarative UI with Jetpack Compose and Material 3
- Clean Architecture split into `data`, `domain`, and `presentation` layers
- MVVM state management with ViewModels, reducers, UI states, UI actions, and one-off effects
- Authentication flow with persisted session state and refresh-token recovery
- REST API integration with Retrofit, OkHttp, Gson, DTOs, mappers, and repository implementations
- Local persistence with Room and DataStore
- Real-time infrastructure with Socket.IO and Kotlin Flow
- Navigation Compose graph structure for auth, session gate, main tabs, search, discovery, messages, and profile flows
- Unit and instrumented tests for use cases, repositories, mappers, interceptors, ViewModels, reducers, Compose UI, Room DAOs, and navigation behavior
- Startup performance instrumentation with AndroidX JankStats and Macrobenchmark scaffolding

## Core Features

### Authentication

- Welcome, login, register, and session gate flows
- Email and password validation
- Password strength calculation
- Session persistence with DataStore
- Bearer token injection for authenticated requests
- Refresh-token handling through a custom OkHttp `Authenticator`
- Session cleanup on unrecoverable authentication failures

### Plate Search

- Turkish plate input formatting and validation
- Plate search through repository/use-case boundaries
- Recent searches backed by Room
- Saved plates backed by Room
- Reactive local updates with Kotlin Flow
- Search UI state reducer and one-off UI effects

### Discovery

- Discovery home data from API responses
- Trend, attention, good driver, and newest plate sections
- City-based stats and recent activity models
- DTO to domain to UI model mapping
- Loading, empty, success, and error states

### Social, Profile, Location, and Messaging Foundations

- User, profile, settings, friendship, social link, review, location, subscription, and chat API service definitions
- Repository implementations for backend-backed features
- Socket.IO data sources for connection state, messaging, and location events
- Token-authenticated socket connection management

## Architecture

PlateMate follows a Clean Architecture style:

```text
app/src/main/java/com/mefy/platemate
|-- core              Shared result, error, pagination, mapper, coroutine, and utility types
|-- data              Local storage, remote services, DTOs, mappers, repositories, sockets
|-- di                Hilt modules for network, database, repositories, dispatchers, UI mappers
|-- domain            Business models, repository contracts, and use cases
|-- presentation      Compose UI, navigation, ViewModels, reducers, UI state, design system
```

The general dependency direction is:

```text
Presentation -> Domain <- Data
```

Presentation depends on domain use cases and UI mappers. Data implements domain repository contracts and maps remote/local models into domain models.

## Tech Stack

| Area | Technologies |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | Clean Architecture, MVVM, Repository pattern, Use Cases |
| Async | Coroutines, Flow |
| Dependency Injection | Hilt, KSP |
| Networking | Retrofit, OkHttp, Gson |
| Local Storage | Room, DataStore Preferences |
| Realtime | Socket.IO |
| Navigation | Navigation Compose |
| Images | Coil |
| Analytics | Firebase Analytics |
| Testing | JUnit, MockWebServer, Compose UI Test, Navigation Testing, Room instrumentation tests |
| Performance | AndroidX JankStats, Macrobenchmark |

## Project Structure

```text
PlateMate
|-- app
|   |-- src
|   |   |-- main
|   |   |   |-- java/com/mefy/platemate
|   |   |   |-- res
|   |   |-- test
|   |   |-- androidTest
|-- docs
|-- gradle
|-- build.gradle.kts
|-- settings.gradle.kts
```

## Getting Started

### Requirements

- Android Studio
- JDK 17
- Android SDK with the configured compile SDK
- Firebase project config if you want to build Google Services enabled variants

### Firebase Configuration

This repository intentionally does not include:

```text
app/google-services.json
```

Create or download your Firebase Android config file and place it at:

```text
app/google-services.json
```

The file is ignored by Git to avoid publishing project-specific Firebase configuration.

### Build

On Windows:

```powershell
.\gradlew.bat :app:compileDebugKotlin
```

On macOS/Linux:

```bash
./gradlew :app:compileDebugKotlin
```

### Run Unit Tests

On Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

On macOS/Linux:

```bash
./gradlew :app:testDebugUnitTest
```

## Testing Scope

The test suite covers:

- Domain validation and formatting use cases
- Repository behavior and safe API call handling
- DTO/domain/UI mapper behavior
- OkHttp authentication, language, and token refresh interceptors
- DataStore session and language preference stores
- Room DAO and repository behavior
- ViewModel state transitions
- Reducer behavior
- Common UI error and dialog handling
- Compose screen behavior
- Navigation graph behavior

## Performance Notes

The project includes debug-only startup jank instrumentation and a Macrobenchmark scaffold:

- `StartupTrace`
- `StartupJankMonitor`
- `StartupSearchMacrobenchmarkTest`

Debug builds can log startup route markers and jank frames, while release builds disable startup jank monitoring by default.

## Security and Repository Hygiene

The repository ignores local, generated, and environment-specific files:

- `local.properties`
- `app/google-services.json`
- `.gradle/`
- `.idea/`
- `.kotlin/`
- `build/`
- `app/build/`

Internal planning and contract notes are also kept out of the public repository.

## Portfolio Summary

PlateMate demonstrates practical Android development skills across UI, architecture, persistence, networking, authentication, realtime communication, testing, and performance awareness. It is intended to show more than screen-building: the codebase includes production-style boundaries, error handling, session management, local-first behavior, and testable feature slices.
