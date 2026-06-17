---
type: android-doc
area: conventions
tags:
  - android
  - conventions
  - clean-code
  - kotlin
aliases:
  - Android Conventions
updated: 2026-05-29
---

# Conventions

## Purpose

Defines naming, package, state-management, mapping, and dependency-injection conventions for the Android client.

## Package Layout

| Concern | Typical location |
| --- | --- |
| Routes | `presentation/features/<feature>/<screen>/<Screen>Route.kt` |
| Screens | `presentation/features/<feature>/<screen>/<Screen>.kt` |
| Components | `presentation/features/<feature>/<screen>/components/*` or shared `presentation/components/*` |
| ViewModels | `presentation/features/<feature>/<screen>/<Screen>ViewModel.kt` |
| UI state/action/effect | `presentation/features/<feature>/<screen>/<Screen>UiState.kt`, etc. |
| Reducers | `presentation/features/<feature>/<screen>/<Screen>StateReducer.kt` |
| UI mappers | `presentation/features/<feature>/<screen>/mapper/*` or feature mapper package |
| Use cases | `domain/usecase/<feature>/*UseCase.kt` |
| Domain models | `domain/model/*` |
| Repository interfaces | `domain/repository/*Repository.kt` |
| Repository implementations | `data/repository/*RepositoryImpl.kt` |
| Retrofit services | `data/remote/api/*ApiService.kt` |
| DTOs | `data/remote/dto/*` |
| Data mappers | `data/mapper/*` |
| Room/DataStore | `data/local/*` |
| Hilt modules | `di/*Module.kt` |

## Naming

| Type | Convention |
| --- | --- |
| Route | `<Feature>Route` |
| Screen | `<Feature>Screen` |
| ViewModel | `<Feature>ViewModel` |
| State | `<Feature>UiState` |
| Action | `<Feature>UiAction` |
| Effect | `<Feature>UiEffect` |
| Reducer | `<Feature>StateReducer` |
| Use case | `<Verb><DomainThing>UseCase` |
| Repository interface | `<Thing>Repository` |
| Repository implementation | `<Thing>RepositoryImpl` |
| Retrofit service | `<Thing>ApiService` |
| DTO | `<Thing>Dto`, `<Thing>Request`, `<Thing>Response` |
| Mapper | `<Source>To<Target>Mapper` or feature-consistent existing style |

Follow nearby existing naming when it is already consistent.

## Kotlin Style

- Prefer immutable data classes for state and models.
- Prefer `val` over `var`.
- Keep functions small and purpose-named.
- Avoid broad utility functions if behavior belongs to a feature.
- Avoid nullable state when a sealed state or explicit default is clearer.
- Avoid magic strings/numbers; use constants or resources when appropriate.
- Do not add formatting-only changes to unrelated files.

## UI State Rules

A feature should generally expose:

```kotlin
data class FeatureUiState(...)

sealed interface FeatureUiAction { ... }

sealed interface FeatureUiEffect { ... }
```

Use:

- `UiState` for persistent render state
- `UiAction` for user/system input
- `UiEffect` for one-time events

Do not store one-time events directly in `UiState` unless the existing feature pattern already does this intentionally.

## ViewModel Rules

A ViewModel should:

- expose immutable `StateFlow<UiState>`
- expose `SharedFlow`/`Channel` based one-time effects when needed
- handle `UiAction`
- call use cases, not Retrofit services/DAOs directly
- map domain results into UI state/effects
- keep coroutine dispatching consistent with project utilities

A ViewModel should not:

- hold Android `Context` unless unavoidable and injected safely
- directly parse DTOs
- directly navigate through `NavController`
- contain large UI formatting logic that belongs in a mapper/helper

## Mapper Rules

- DTO/entity → domain mapping belongs in `data`.
- Domain → UI mapping belongs in `presentation`.
- Avoid mapping inside Composables.
- Keep mapper behavior deterministic and testable.
- Handle nullable backend fields with explicit defaults or explicit error mapping.

## DI Rules

- Use Hilt modules for binding interfaces to implementations.
- Inject interfaces where practical.
- Avoid service locator patterns.
- Do not create Retrofit/Room/DataStore clients manually inside feature code.

## Resource Rules

- User-facing strings go to `strings.xml`.
- Reusable dimensions/colors should use theme/design-system tokens.
- Preview-only text may remain local if clearly preview/debug only.

## Documentation Rules

- Docs live under `docs/` and use Obsidian links such as [[architecture]].
- Keep docs short and task-routable.
- Use [[improvement-backlog]] for cleanup items.
- Update docs when behavior, API contracts, architecture, or conventions change.
