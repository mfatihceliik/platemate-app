# Feature Development Playbook

## Choose the Feature Type First

Before following the full sequence, classify the task:

### API-backed feature
Use this when the feature depends on backend data or sends data to the backend.
Follow the full Data → Domain → Presentation sequence.

### UI-only feature
Use this for screens, components, visual states, or layout changes that do not require backend or local persistence.
Skip Data and Domain unless needed. Start from UiState, Screen, Route, and Design System.

### Local-only feature
Use this when the feature depends on DataStore, Room, or local cache.
Start from the local data source, then map to Domain if the data affects business logic.

### Refactor-only task
Do not follow the full feature sequence.
Modify only the requested scope and preserve existing behavior.

---

## API-Backed Feature Sequence

When building an API-backed feature, strictly follow this bottom-up sequence.

### 1. Data Layer (Bottom)
1. **Contract:** Check `CLIENT_DTO_CONTRACT.md` for the backend payload.
2. **DTOs:** Create Request/Response DTOs in `data/remote/dto/...`.
3. **Mappers:** Create DTO -> Domain mappers in `data/mapper/`.
4. **Repository Interface:** Define the contract in `domain/repository/`.
5. **Repository Implementation:** Implement the interface in `data/repository/`. Handle API calls and return `AppResult`.
6. **Hilt Binding:** Bind the interface to implementation in `di/RepositoryModule.kt`.

### 2. Domain Layer (Middle)
7. **UseCase (Optional but Default):** Create UseCases in `domain/usecase/...` if logic is needed or it's a user-facing action. Inject the repository.

### 3. Presentation Layer (Top)
8. **State Definitions:** Create `UiState`, `UiAction`, and `UiEffect` in `presentation/{feature}/model/`.
9. **Presentation Mapper:** If Domain models are complex, map them to `UiModel` in `presentation/{feature}/mapper/`.
10. **ViewModel:** Create the ViewModel in `presentation/{feature}/`. Inject UseCases. Handle state transitions, loading, and error mapping. Add `@HiltViewModel`.
11. **Screen Component:** Create the stateless `*Screen` composable. Add a `@Preview`.
12. **Route Component:** Create the stateful `*Route` composable. Collect state, handle effects, and call `*Screen`.
13. **Navigation:** Register the Route in the `AppNavHost` and `presentation/navigation/` files.

---

## UI-only Feature Sequence

1. Check existing feature pattern.
2. Define or update `UiState`, `UiAction`, and `UiEffect` only if needed.
3. Create or update stateless `*Screen`.
4. Create or update `*Route` if ViewModel/effect collection is needed.
5. Reuse existing `PM*` components before creating new ones.
6. Follow `design-system-guidelines.md`.
7. Add previews for screen-level and reusable components.

---

## Verification
Run the build and check for errors. Verify testability.
