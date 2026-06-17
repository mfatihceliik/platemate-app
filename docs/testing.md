---
type: android-doc
area: testing
tags:
  - android
  - testing
  - unit-tests
  - compose-tests
aliases:
  - Android Testing
updated: 2026-05-29
---

# Testing

## Purpose

Documents recommended testing strategy. The inspected archive did not include test source sets, so these are target conventions for future work.

## Test Priority

Add tests in this order:

1. reducers
2. use cases with non-trivial rules
3. mappers
4. ViewModels
5. repositories with fake API/local sources
6. navigation/Compose UI tests for critical flows

## What to Test

| Area | Test focus |
| --- | --- |
| Reducers | Pure state transitions |
| Use cases | Input normalization, validation, repository delegation, business rules |
| Mappers | DTO/entity → domain, domain → UI, null/default behavior |
| ViewModels | Action handling, loading/success/error state, effects |
| Repositories | safe API call mapping, mapper usage, cache/session side effects |
| Token/session | Refresh success/failure, logout, session clear behavior |
| Compose | Important screen states and user actions |

## ViewModel Test Pattern

Use coroutine test dispatchers and fake use cases/repositories.

Validate:

- initial state
- action → loading state
- success → content state/effect
- error → error state/event
- duplicate loading guards

## Reducer Test Pattern

Reducers should be pure and directly unit-testable.

Example expectations:

- `onSubmitLoading` sets loading and clears previous field errors if appropriate
- `onSubmitError` stops loading and stores message/field errors
- `onContentLoaded` stops loading/refreshing and stores content

## Mapper Test Pattern

Test:

- all required fields map correctly
- nullable backend fields use safe defaults
- enum/status/code mapping is stable
- invalid/missing data does not crash unexpectedly

## Compose Test Pattern

Use Compose UI tests for:

- login/register form validation
- search input/search click
- bottom bar visibility
- navigation from auth to main
- loading/empty/error states

Prefer testing screens with fake state instead of real ViewModels unless integration behavior is needed.

## Recommended Commands

Verify actual Gradle tasks from build files. Common commands may include:

```bash
./gradlew test
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew lint
```

On Windows:

```powershell
.\gradlew.bat test
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lint
```

## Agent Rule

When changing behavior, mention which tests should be added or run even if test files are not yet present.
