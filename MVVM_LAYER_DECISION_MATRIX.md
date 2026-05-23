# MVVM Layer Decision Matrix

Last updated: 2026-05-18

Use this file as a quick ownership rule for `data`, `domain`, and `presentation`.

## Domain
- Put business rules and validation logic here.
- Put app concepts that stay meaningful even if UI changes (value objects, domain enums, policies).
- Keep this layer framework-agnostic (no Android/Compose/Retrofit/Gson types).

## Presentation
- Put screen state, UI actions/effects, and ViewModel orchestration here.
- Put UI-only enums/models here (filter chip types, icon mapping, visual state).
- Any type that knows `R.*`, `ImageVector`, Compose concerns, or navigation belongs here.

## Data
- Put DTO/request/response models and wire contracts here.
- Put API/local datasource logic and DTO -> Domain mapping here.
- Parse backend string/code values into typed domain values here.

## Quick Decision Rule
1. If it knows UI resources, icons, routes, or composables: `presentation`.
2. If it mirrors JSON/wire shape: `data`.
3. If it represents pure business meaning: `domain`.

## Anti-Patterns to Avoid
- `domain` importing from `presentation` or `data`.
- `presentation` importing from `data` DTOs directly.
- Storing backend raw string codes in UI logic when they can be typed in domain via mapper.
