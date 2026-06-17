---
type: android-doc
area: feature-patterns
tags:
  - android
  - mvvm
  - feature-patterns
  - state-management
aliases:
  - Android Feature Patterns
  - Feature Patterns
updated: 2026-05-29
---

# Feature Patterns

## Purpose

Defines the standard feature shape for MVVM + Compose work in this project.

## Standard Feature Flow

```text
Route -> Screen -> ViewModel -> UseCase -> Repository Interface -> Repository Implementation -> API/Local Source
```

The route/screen boundary is important:

- Route knows ViewModel and navigation callbacks.
- Screen knows only state and callbacks.
- ViewModel knows use cases.
- Use cases know repository interfaces.
- Repository implementations know data sources and mappers.

## Recommended Feature File Shape

```text
presentation/features/<feature>/<screen>/
  <Screen>Route.kt
  <Screen>Screen.kt
  <Screen>ViewModel.kt
  <Screen>UiState.kt
  <Screen>UiAction.kt
  <Screen>UiEffect.kt
  <Screen>StateReducer.kt        # when state transitions are non-trivial
  mapper/                        # when domain -> UI mapping is non-trivial
  components/                    # screen-specific components
```

Domain/data side:

```text
domain/usecase/<feature>/
  <UseCase>.kt

domain/repository/
  <Feature>Repository.kt

data/repository/
  <Feature>RepositoryImpl.kt

data/remote/api/
  <Feature>ApiService.kt

data/remote/dto/
  request/response DTOs

data/mapper/
  DTO/entity -> domain mappers
```

## Route Responsibilities

A Route composable may:

- obtain ViewModel via Hilt
- collect `uiState` with `collectAsStateWithLifecycle()`
- collect one-time `uiEffect`
- call navigation callbacks
- pass state and action callbacks to Screen

A Route composable should not:

- render complex UI directly
- perform business validation
- call repositories/use cases
- hold large local state

## Screen Responsibilities

A Screen composable may:

- render UI from `UiState`
- call `onAction(...)` callbacks
- manage small local UI-only state such as password visibility
- contain preview data

A Screen composable should not:

- know about ViewModel
- collect flows
- navigate directly
- call use cases/repositories
- parse backend DTOs

## ViewModel Responsibilities

A ViewModel may:

- expose `StateFlow<UiState>`
- emit `UiEffect`
- process `UiAction`
- call use cases
- update state through reducers/private helpers
- map domain errors to UI messages

A ViewModel should not:

- directly call Retrofit/Room/DataStore
- expose mutable state
- contain large Composable-specific formatting logic
- pass DTOs to UI

## Reducer Usage

Use a reducer when state transitions are repeated or complex.

Good reducer responsibilities:

- loading/content/error transitions
- form field update transitions
- validation result application
- refresh/pagination state transitions

Avoid reducers for one-line state changes if they reduce readability.

## Use Case Usage

Use cases should contain app/business-level decisions, validation, normalization, or orchestration.

Good use cases:

- validate Turkish plate input
- login/register/logout
- refresh session
- search plate
- load profile
- send chat message

Do not create empty pass-through use cases unless the project already uses that pattern for consistency.

## Model Boundary

| Boundary | Mapping |
| --- | --- |
| API/local → domain | data mapper |
| domain → UI | presentation mapper/helper |
| UI input → domain request | ViewModel/use case request model when needed |

Keep DTOs out of presentation and Composables.

## One-Time Events

Use `UiEffect` for:

- navigation
- snackbars/toasts
- focus requests
- dialog commands
- session-expired redirect

Do not store one-time navigation flags inside persistent `UiState` unless existing feature style requires it.
