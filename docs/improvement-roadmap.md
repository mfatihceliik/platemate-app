# Improvement Roadmap

This roadmap identifies potential architectural improvements and areas for future optimization. **Do not execute large refactors based on this document without explicit user approval.**

## Priority 1 - Architectural Safety
* **Problem:** As features grow, ViewModels can become bloated "God Objects" if UseCases are bypassed.
* **Why it matters:** Hard to test, hard to maintain, violates Single Responsibility Principle.
* **Proposed Solution:** Strictly enforce the rule that any logic beyond simple state emission should be moved to a UseCase or a Presentation Mapper.
* **How to detect:** ViewModels exceeding 300-400 lines, or containing complex `if/else` logic that transforms data instead of just passing it.
* **Risk Level:** Low
* **Relevant packages:** `presentation/*/*ViewModel.kt`, `domain/usecase/`

## Priority 2 - Compose and State Management
* **Problem:** Complex screens might handle too many generic states, leading to messy recomposition logic.
* **Why it matters:** Performance drops, janky UI.
* **Proposed Solution:** Utilize State Reducers (`StateReducer.kt`) more heavily for screens with complex state transitions (like Search or Discover). Ensure `UiState` objects are as flat as possible.
* **How to detect:** `UiState` objects with deeply nested properties or ViewModels with multiple `MutableStateFlow`s instead of a single consolidated state flow.
* **Risk Level:** Medium
* **Relevant packages:** `presentation/*/reducer/`

## Priority 3 - Data and Error Handling
* **Problem:** Scattered mapping logic or inconsistent error translations from backend.
* **Why it matters:** Inconsistent user experience when errors occur.
* **Proposed Solution:** Standardize a single UI Error Mapper to convert `AppError` directly to localized `String` resources so ViewModels never handle raw strings for errors.
* **How to detect:** Hardcoded error string assignments in ViewModels or duplicate `AppError` to string mapping logic across different ViewModels.
* **Risk Level:** Low
* **Relevant packages:** `presentation/mapper/`, `core/error/`

## Priority 4 - Testing
* **Problem:** UI and integration testing in Compose can fall behind unit testing.
* **Why it matters:** Business logic may work, but the UI could fail to reflect it properly.
* **Proposed Solution:** Implement snapshot testing (e.g., Paparazzi or Roborazzi) for isolated Compose components to catch visual regressions automatically.
* **How to detect:** Checking `app/src/androidTest` or `app/src/test` and finding only UseCase unit tests with no visual/component verification tests.
* **Risk Level:** Low
* **Relevant packages:** `presentation/components/`, `app/src/androidTest/`
