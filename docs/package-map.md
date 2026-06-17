# Package Map

This document maps the structure of `com.mefy.platemate` to help agents navigate the project quickly.

## `core`
**Responsibility:** Shared abstractions, utilities, and infrastructure types. No feature-specific business logic.
* `core/common/result`: `AppResult`, `DataResultResponse` wrappers.
* `core/common/pagination`: Pagination meta models.
* `core/coroutine`: Dispatcher abstractions (`AppDispatchers`).
* `core/error`: Custom app error definitions (`AppError`).
* `core/mapper`: Base mapper interfaces.
* `core/navigation`: Shared navigation utilities.
* `core/util`: Constants and utility extensions.

## `data`
**Responsibility:** Remote/local data access and repository implementations.
* `data/remote/dto`: API response and request shapes (matches `CLIENT_DTO_CONTRACT.md`).
* `data/remote/rest`: API interfaces (e.g., `AuthApiService`, `PlateApiService`).
* `data/remote/interceptor`: Auth tokens, languages, refresh logic.
* `data/remote/websocket`: Socket connection and data source implementations.
* `data/local`: Room Database, DAOs, Entities, DataStore preferences.
* `data/mapper`: Classes responsible for mapping DTOs/Entities to Domain models.
* `data/repository`: Implementations of domain repository interfaces.

## `domain`
**Responsibility:** Business models, repository contracts, and UseCases. Pure Kotlin logic.
* `domain/model`: App domain entities (e.g., `User`, `PlateDetail`, `ChatRoom`). No framework imports.
* `domain/repository`: Interfaces that `data` must implement.
* `domain/usecase`: Feature-specific business logic divided by folder (e.g., `auth`, `chat`, `review`).

## `presentation`
**Responsibility:** UI, ViewModels, Reducers, UiState, Navigation, and Design System.
* `presentation/navigation`: AppNavHost, routes, destinations.
* `presentation/theme`: Colors, typography, spacing, semantic tokens.
* `presentation/{feature}`: Grouped by feature (e.g., `auth`, `discover`, `profile`, `search`).
  * `components/`: Local compose components.
  * `mapper/`: Presentation UI mappers (Domain -> UiModel).
  * `model/`: UI-specific models (`UiState`, `UiModel`, `UiAction`, `UiEffect`).
  * `reducer/`: State transition logic (`StateReducer`).
  * `*Screen.kt` / `*Route.kt`: Compose UI and Navigation entry points.
  * `*ViewModel.kt`: Feature ViewModel.

## `di`
**Responsibility:** Hilt modules for Dependency Injection.
* `AppModule`, `NetworkModule`, `LocalDatabaseModule`, `RepositoryModule`, etc. Keep bindings logically separated.
