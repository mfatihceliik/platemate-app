# MVVM Guidelines

This project utilizes Model-View-ViewModel (MVVM) heavily.

## ViewModel Responsibilities
* Orchestrate data from `domain` (UseCases/Contracts) to the `presentation` layer.
* Expose immutable `UiState` to Compose.
* Process `UiAction` events from Compose.
* Emit one-shot `UiEffect` events for Compose to handle (e.g., Navigation, Snackbars).
* Manage Coroutine scopes and handle asynchronous data loading.

## Strict Rules
* **No DTOs:** ViewModels must NEVER know about or import DTOs from `data.remote.dto`. They should only use Domain models.
* **No Implementations:** ViewModels should depend on UseCases or Repository interfaces, NEVER on Repository implementations directly.
* **State Management:** Use `StateFlow` to expose `UiState`. Ensure `UiState` is immutable (data class).
* **Side Effects:** One-shot events like showing a toast or triggering navigation MUST be sent via `Channel` / `SharedFlow` exposed as `UiEffect`. Do not use state variables like `navigateToDetails = true` because they are error-prone on recomposition.
* **Reducers:** If the screen has complex state transitions, use a Reducer class (e.g., `DiscoverStateReducer`) to calculate the new state, keeping the ViewModel lean.
* **Fat ViewModel Anti-pattern:** If a ViewModel is doing complex formatting or business logic calculations, extract that logic to a UseCase or a Presentation Mapper.

## Route / Screen Content Separation
* ViewModels should be injected and scoped at the **Route** level (e.g., `DiscoverRoute`), NOT the **Screen** level.
* Pass only the required state and lambda callbacks to the actual `*Screen` composable. This makes the `*Screen` composable previewable and testable without a real ViewModel instance.

*See also: [[compose-guidelines]], [[state-navigation-guidelines]]*
