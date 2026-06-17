---
type: android-doc
area: compose-ui
tags:
  - android
  - jetpack-compose
  - ui
  - best-practices
aliases:
  - Compose UI
  - Jetpack Compose Conventions
updated: 2026-05-29
---

# Compose UI

## Purpose

Documents Jetpack Compose conventions for this project.

## Current UI Pattern

The project already uses:

- Route composables for ViewModel/state/effect collection.
- Stateless Screen composables for UI rendering.
- Shared `PM*` design-system components.
- Theme extensions such as dimensions and semantic colors.
- Shimmer loading content in several screens.
- Lazy lists with keys/content types in some places.

Preserve this direction.

## Route Collection

Use lifecycle-aware state collection:

```kotlin
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

Avoid route/root-level `collectAsState()` for ViewModel `StateFlow` unless there is a specific reason.

Collect effects with `LaunchedEffect(viewModel)` or another stable key:

```kotlin
LaunchedEffect(viewModel) {
    viewModel.uiEffect.collectLatest { effect ->
        when (effect) {
            is FeatureUiEffect.Navigate -> onNavigate(effect.id)
        }
    }
}
```

## State Hoisting

- ViewModel owns screen state.
- Route connects ViewModel to Screen.
- Screen receives state and callbacks.
- Local `remember` state is only for visual/temporary concerns.
- Use `rememberSaveable` for local user-editable state that should survive recreation.

## Large Screen Rule

Avoid very large screen files. When a screen grows, split by responsibility:

```text
<Feature>Screen.kt
components/<Feature>Header.kt
components/<Feature>Content.kt
components/<Feature>EmptyState.kt
components/<Feature>LoadingState.kt
components/<Feature>ErrorState.kt
components/<Feature>ListItem.kt
components/<Feature>Shimmer.kt
```

Current files that should be gradually split when touched:

- `DiscoverScreen.kt`
- `ProfileScreen.kt`
- `SearchScreen.kt`
- `PlateCard.kt`
- `WelcomeScreen.kt`

Do not refactor all large screens at once unless the task explicitly asks for UI cleanup.

## Design System Usage

Prefer existing project components before raw Material components:

- `PMText`
- `PMButton`
- `PMTextField`
- existing cards/list items
- project theme tokens
- project spacing/dimensions/radius/elevation abstractions

Use raw Material components only when there is no suitable project component or when creating a new reusable project component.

## Lazy Lists

For `LazyColumn`, `LazyRow`, or grids:

- use stable keys when item IDs exist
- use `contentType` for mixed item types when useful
- avoid heavy computation inside item lambdas
- pass only required callbacks/data to item composables

Example:

```kotlin
items(
    items = state.items,
    key = { it.id },
    contentType = { "plate_item" }
) { item ->
    PlateItem(
        item = item,
        onClick = { onAction(FeatureUiAction.ItemClick(item.id)) }
    )
}
```

## Side Effects

Use side-effect APIs carefully:

| API | Use for |
| --- | --- |
| `LaunchedEffect` | collecting effects, initial load trigger when needed |
| `DisposableEffect` | register/unregister lifecycle callbacks |
| `rememberUpdatedState` | latest callback inside long-running effect |
| `derivedStateOf` | derived state that prevents unnecessary recomputation |

Do not use side effects for business logic that belongs in ViewModel.

## Previews

Previews should:

- use fake/static state
- avoid real ViewModels
- avoid network/data dependencies
- show meaningful loading/content/error variants when practical

## Accessibility

- Provide `contentDescription` for meaningful icons/images.
- Use `null` content description for purely decorative visuals.
- Keep touch targets reasonably sized.
- Avoid color-only state indicators.

## Common Mistakes to Avoid

- `NavController` inside Screen.
- Flow collection inside Screen.
- Repository/use case calls inside Composables.
- DTO parsing in UI.
- Large nested Composable functions with many responsibilities.
- Local `remember` state duplicating ViewModel state.
- Hardcoded user-facing strings.
