# State and Navigation Guidelines

This document outlines how State, Events, Effects, and Navigation must be handled in the PlateMate project.

## The Triad: State, Action, Effect
We use a strict MVI-like Triad for screen communication:

1. **`UiState` (State):** 
   * Must be an immutable data class.
   * Represents the *entire* state of the screen (e.g., loading, data, empty, error).
   * Passed down from ViewModel to the Compose Screen.
2. **`UiAction` (Event):**
   * Represents user intents or lifecycle events (e.g., `OnLoginClicked`, `OnQueryChanged`).
   * Passed up from the Compose Screen to the ViewModel via a single `onAction: (UiAction) -> Unit` lambda.
3. **`UiEffect` (Side Effect):**
   * Represents one-shot events that should only happen once (e.g., Navigation, showing a Toast, Snackbar).
   * Emitted from the ViewModel via a Channel and collected in the Compose `*Route` using `LaunchedEffect`.

## Navigation Rules
* **No NavController in ViewModel:** ViewModels must NEVER know about `NavController` or Android Navigation routes. They make the *decision* to navigate and emit a `UiEffect` (e.g., `NavigateToHome`).
* **Handling Effects:** The Compose `*Route` collects the `UiEffect` and calls the `NavController` directly.
* **Argument Passing:** Prefer passing primitives (IDs, codes) via Route arguments. If complex objects need to be passed, consider using a shared ViewModel or fetching the object again in the destination screen using the ID.

*See also: [[compose-guidelines]], [[mvvm-guidelines]]*
