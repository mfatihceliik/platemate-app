# Naming Conventions

Consistency in naming ensures fast agent and human navigation.

## Presentation Layer
* **ViewModel:** `[Feature]ViewModel` (e.g., `DiscoverViewModel`).
* **State/Event/Effect:** 
  * State: `[Feature]UiState` (e.g., `ProfileUiState`).
  * Action (From UI to VM): `[Feature]UiAction` (e.g., `SearchUiAction`).
  * Effect (From VM to UI): `[Feature]UiEffect` (e.g., `LoginUiEffect`).
* **Composables:**
  * Route Component (Stateful): `[Feature]Route` (e.g., `DiscoverRoute`).
  * Screen Component (Stateless): `[Feature]Screen` (e.g., `DiscoverScreen`).
  * Shared Components: Start with `PM` if they are core design system pieces (e.g., `PMButton`, `PMTextField`), otherwise standard pascal case (e.g., `ProfileTopBar`).

## Domain Layer
* **UseCase:** `[Action][Subject]UseCase` (e.g., `GetProfileUseCase`, `UpdateSettingsUseCase`).
* **Repository Interface:** `[Domain]Repository` (e.g., `UserRepository`).
* **Models:** Standard nouns without suffixes (e.g., `User`, `PlateDetail`).

## Mapper Naming

Avoid generic mapper names like `UserMapper` when multiple mapping directions can exist.

### Data Layer Mappers
DTO to Domain:
```text
[Subject]DtoMapper
```
Examples:
* `UserDtoMapper`
* `PlateDetailDtoMapper`

Entity to Domain:
```text
[Subject]EntityMapper
```
Examples:
* `UserEntityMapper`
* `PlateEntityMapper`

### Presentation Layer Mappers
Domain to UiModel:
```text
[Subject]UiMapper
```
Examples:
* `ProfileUiMapper`
* `PlateDetailUiMapper`
* `SearchResultUiMapper`

## Data Layer
* **DTO:** `[Subject]Dto` or `[Action]Request` (e.g., `UserDto`, `LoginRequest`).
* **Repository Implementation:** `[Domain]RepositoryImpl` (e.g., `UserRepositoryImpl`).

## Dependency Injection
* **Hilt Module:** `[Context]Module` (e.g., `NetworkModule`, `RepositoryModule`).

## Tests
* **Test Class:** `[ClassUnderTest]Test` (e.g., `LoginUseCaseTest`).
* **Test Method:** `[methodName]_[condition]_[expectedResult]` (e.g., `login_invalidPassword_returnsError`).
