---
type: android-doc
area: agent-workflow
tags:
  - android
  - ai-agent
  - workflow
  - token-budget
aliases:
  - Agent Workflow
  - Android Agent Workflow
updated: 2026-05-29
---

# Agent Workflow

## Goal

Help AI agents make focused Android changes with low token usage and low architectural risk.

## Default Workflow

1. Understand the user request.
2. Classify the task using [[index]].
3. Read only the relevant docs.
4. Search exact source symbols/packages.
5. Inspect the smallest related file set.
6. Compare with the closest existing implementation.
7. Make the smallest correct change.
8. Update tests/docs only when needed.
9. Summarize changed files and validation.

## Context Budget Rules

Do not read the full project by default.

Initial context should usually be limited to:

- `AGENTS.md`
- `docs/index.md`
- 1–3 task-specific docs
- source files directly related to the change

Large files such as `DiscoverScreen.kt`, `ProfileScreen.kt`, `SearchScreen.kt`, `PlateCard.kt`, and `WelcomeScreen.kt` should be searched by function name first. Read only the touched section unless the task is screen-wide refactoring.

## When to Stop Searching

Stop opening more files when you have identified:

- the route/screen entry point
- the ViewModel state/action/effect flow
- the relevant use case/repository/API path, if data is involved
- the mapper/DTO/model touched by the change
- the nearest existing pattern to copy

## Feature Change Procedure

### UI-only change

Inspect:

1. `*Route.kt`
2. `*Screen.kt`
3. related component or theme file
4. strings/resources

Do not inspect repositories or networking unless the UI change depends on data shape.

### ViewModel behavior change

Inspect:

1. `*ViewModel.kt`
2. `*UiState.kt`
3. `*UiAction.kt`
4. `*UiEffect.kt`
5. reducer/mapper if present
6. relevant use case

Do not change repository/data code unless the behavior requires different data.

### API integration change

Inspect:

1. Retrofit service
2. request/response DTOs
3. repository interface
4. repository implementation
5. mapper
6. use case
7. consuming ViewModel
8. backend contract if available

Do not expose DTOs to UI just to move faster.

### Navigation change

Inspect:

1. destination definitions
2. feature graph
3. route composable
4. ViewModel effect that requests navigation
5. `AppState` / bottom bar logic if top-level behavior changes

Do not pass `NavController` into Screens.

## Refactor Rules

- Refactor only the touched area.
- Split large Composables gradually.
- Do not rename packages/classes unless the task requires it.
- Do not introduce new architecture patterns without clear benefit.
- Prefer extraction of private composables/helpers before creating broad abstractions.

## Validation Checklist

Before final response:

- Explain what changed.
- Mention affected files.
- Mention whether docs/tests were updated.
- Mention commands that should be run if build files are available.
- Mention any uncertainty or files not inspected.

## Anti-Patterns

Avoid:

- scanning the whole repo for a one-screen UI tweak
- changing backend contracts from Android code assumptions
- adding a new global state pattern for a local feature
- moving all screens/components during a small task
- mixing DTO/domain/UI models
- collecting flows in Screens instead of Routes
- handling navigation directly inside Screens
