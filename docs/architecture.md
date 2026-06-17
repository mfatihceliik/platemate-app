---
type: android-doc
area: architecture
tags:
  - android
  - architecture
  - clean-architecture
  - mvvm
  - hilt
aliases:
  - Android Architecture
updated: 2026-05-29
---

# Architecture

## Purpose

Documents the current Android client architecture and the dependency rules agents must preserve.

## Current Stack

Kotlin, Jetpack Compose, MVVM, Hilt, Retrofit/OkHttp, Gson, Room, DataStore, Kotlin Coroutines/Flow, Navigation Compose, Socket.IO client, and Firebase-related integrations.

The inspected archive mainly contained `src/main` style app code. Build files and tests were not available in the inspected archive, so commands and dependency versions must be verified from source when available.

## Layer Map

| Layer | Package | Responsibility |
| --- | --- | --- |
| App entry | `com.mefy.platemate` | `Application`, `MainActivity`, root app setup |
| Core | `core` | `AppResult`, `AppError`, dispatchers, shared result wrappers, pagination, mapper helpers, constants |
| Data | `data` | Retrofit services, DTOs, Room, DataStore, websocket data sources, repository implementations, data mappers |
| Domain | `domain` | Domain models, repository interfaces, use cases, business/application rules |
| Presentation | `presentation` | Compose UI, routes, ViewModels, UI state/action/effect, reducers, UI mappers, navigation, theme/components |
| DI | `di` | Hilt modules for repositories, network, local DB, sockets, mappers, dispatchers |

## Dependency Direction

Allowed:

```text
presentation -> domain -> core
presentation -> core
presentation -> Android/Compose framework

data -> domain -> core
data -> core
data -> Retrofit/Room/DataStore/OkHttp/Socket framework

di -> all layers for wiring only
```

Not allowed:

```text
domain -> data
domain -> presentation
domain -> Android Context / Compose / Retrofit / Room
core -> data/domain/presentation feature code
presentation ViewModel -> data DTOs/entities directly
Composable -> repository/use case directly
```

## Request/Data Flow

Typical remote feature flow:

1. Screen emits `UiAction`.
2. ViewModel handles action.
3. ViewModel calls use case.
4. Use case calls domain repository interface.
5. Data repository implementation calls Retrofit/Room/DataStore/socket data source.
6. Data mapper converts DTO/entity to domain model.
7. Repository returns `AppResult<DomainModel>`.
8. ViewModel maps domain model to UI model if needed.
9. Reducer updates immutable `UiState`.
10. Route observes `UiState` and one-time `UiEffect`.
11. Screen renders state and delegates user actions.

Related docs: [[feature-patterns]], [[data-networking]], [[compose-ui]].

## Current Strengths Observed

- Clear `core/data/domain/presentation/di` package separation.
- Domain repository interfaces are in `domain.repository`.
- Repository implementations are in `data.repository`.
- Retrofit DTOs are kept in `data.remote.dto`.
- Domain models are separate from DTOs.
- Many features use Route → Screen → ViewModel separation.
- Several features use `UiState`, `UiAction`, `UiEffect`, reducers, and UI mappers.
- Type-safe Navigation Compose destinations are present.

## Main Risks to Avoid

- Letting DTOs leak into presentation.
- Calling repositories or use cases directly from Composables.
- Placing Android framework types in domain models/use cases.
- Putting navigation directly inside Screens.
- Expanding large screen files instead of splitting touched sections.
- Adding new abstractions before checking existing project patterns.
- Reading the whole project for small tasks.

## Acceptable Simplicity

This project should remain a practical layered Android app. Do not introduce heavy architecture patterns unless explicitly requested and justified.

Avoid by default:

- full modularization by Gradle module
- CQRS
- event-sourcing style architecture
- one-use-case-class-per-click if it adds no clarity
- global mediator systems
- complex generic MVI framework

Prefer:

- MVVM
- immutable state
- small reducers/helpers
- focused use cases
- repository interfaces in domain
- DTO/domain/UI mapper boundaries
- reusable Compose components
