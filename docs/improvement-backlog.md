---
type: android-doc
area: improvement-backlog
tags:
  - android
  - backlog
  - cleanup
  - quality
aliases:
  - Android Improvement Backlog
  - Improvement Backlog
updated: 2026-05-29
---

# Improvement Backlog

## Purpose

Tracks practical cleanup targets observed from the current Android source. This is not a heavy architecture roadmap. Use it to prevent repeated mistakes and to guide focused refactors.

## High Priority

### Lifecycle-aware collection

Replace route/root `collectAsState()` calls with `collectAsStateWithLifecycle()` where ViewModel `StateFlow` is collected.

Observed files:

- `presentation/navigation/AppNavHost.kt`
- `presentation/features/auth/login/LoginRoute.kt`
- `presentation/features/auth/register/RegisterRoute.kt`
- `presentation/features/auth/sessiongate/SessionGateRoute.kt`

### Large screen/component files

Gradually split very large UI files when touched:

- `DiscoverScreen.kt`
- `ProfileScreen.kt`
- `SearchScreen.kt`
- `PlateCard.kt`
- `WelcomeScreen.kt`

Suggested split style:

```text
components/Header.kt
components/Content.kt
components/ListSection.kt
components/EmptyState.kt
components/ErrorState.kt
components/Shimmer.kt
```

Do not refactor all files at once unless the task is explicitly a UI cleanup.

### API contract verification

Verify these before building new features on top of them:

- `PlateApiService.searchPlateByPath`
- `LocationApiService` endpoints
- placeholder detail destinations in discover/messages graphs

## Medium Priority

### Build configuration for endpoints

Local endpoint constants should move to BuildConfig fields/flavors when build files are available.

### Cleartext traffic

`AndroidManifest.xml` may currently enable cleartext traffic. Keep this for local development only; use environment-specific configuration for production.

### Tests

The inspected archive did not include tests. Add focused tests for:

- reducers
- mappers
- use cases
- ViewModels
- token/session behavior

### Error UX consistency

Some ViewModels may resolve errors through injected `UiErrorResolver`; others may use default `BaseViewModel()` behavior. Prefer consistent injected error resolution for user-facing features.

## Low Priority

### Preview coverage

Add previews for reusable components and important screen states.

### Component extraction

When repeated UI patterns appear in multiple screens, extract shared components only after confirming repetition.

### Documentation maintenance

Update docs only when behavior, architecture, or conventions change. Avoid documentation churn for small local UI text changes.

## Rule for Agents

Use this backlog as guidance, not as permission for broad refactors. If the user asks for a focused task, fix only the relevant backlog item when it is directly connected to the task.
