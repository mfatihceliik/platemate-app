# Error & Result Handling

This project relies on explicit result types rather than throwing exceptions across architectural boundaries.

## The `AppResult` Wrapper
The `core.common.result.AppResult<T>` class (or equivalent) is the standard way to return data from the Domain/Data layers.
* **Success:** Returns the expected Domain Model.
* **Error:** Returns an `AppError` type.

## Exception Boundaries
* **Data Layer Containment:** Exceptions originating from frameworks (e.g., `RetrofitException`, `IOException`, `RoomException`) MUST NOT leak outside the Data layer.
* **Boundary Conversion:** At the Repository and UseCase boundary, all caught exceptions must be converted into an `AppResult.Error(AppError)`.

## `AppError` Hierarchy
Errors are mapped into meaningful types within `core.error.AppError`.
* **Network Error:** Connectivity issues.
* **Unauthorized:** Handled by interceptors, usually results in a session refresh or logout.
* **Validation Error:** Backend validation failure (HTTP 400).
* **Unknown/Server Error:** HTTP 500 or unhandled exceptions.

## UI Handling Rules
1. **Never throw exceptions for control flow:** Use `AppResult` consistently.
2. **UI State Mapping:** ViewModels receive `AppResult`. 
   * On success: Update `UiState` with data, set `isLoading = false`.
   * On error: Update `UiState` with an error model, set `isLoading = false`.
3. **No Technical Messages in UI:** Never show raw backend error strings directly to the user unless they are localized, user-friendly validation messages. Map `AppError` to localized `R.string.*` resources.

*See also: [[mvvm-guidelines]], [[data-domain-guidelines]]*
