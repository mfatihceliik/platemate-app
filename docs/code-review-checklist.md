# Code Review Checklist

Agents and Developers MUST run through this checklist before considering a task complete.

## Agent Workflow
* [ ] Did the agent read only the task-specific docs from `docs/index.md`?
* [ ] If this was a single-agent task, did it plan, implement, and verify appropriately?
* [ ] If this was a multi-agent task, did the agent stay within its assigned role?

## Feature Type & Development
* [ ] Was the task classified correctly as API-backed, UI-only, local-only, or refactor-only?
* [ ] Were unnecessary Data/Domain layers avoided for UI-only changes?
* [ ] Does the implementation follow the sequence in [[feature-development-playbook]]?
* [ ] Has the existing feature pattern been followed for this specific task?

## Mapper Naming
* [ ] Are mappers named according to their mapping direction?
  * DTO → Domain: `*DtoMapper`
  * Entity → Domain: `*EntityMapper`
  * Domain → UiModel: `*UiMapper`

## Architecture & Layers
* [ ] Does the dependency direction strictly follow `presentation -> domain <- data`?
* [ ] Did any DTOs leak into `presentation` or `domain`? (They shouldn't).
* [ ] Are Domain models free of any Android or framework-specific imports?
* [ ] Is the ViewModel communicating with `domain` (UseCase/Repository interface) rather than a concrete `data` repository?
* [ ] Are all exceptions bounded at the Data Layer and converted to `AppResult`?

## Compose, UI & Design System
* [ ] Are new UI components stateless and hoisted where possible?
* [ ] Do new screens (`*Screen.kt`) and reusable components have a `@Preview`?
* [ ] Are there any "Magic Numbers" (e.g., hardcoded `16.dp`) instead of Theme tokens? Check [[design-system-guidelines]].
* [ ] Are strings hardcoded instead of using `stringResource`?
* [ ] Is the `Modifier` passed as the first optional parameter to custom composables?

## State & ViewModels
* [ ] Is `UiState` a completely immutable data class?
* [ ] Are navigation events or one-off events handled via `UiEffect` rather than state flags?
* [ ] Is the ViewModel devoid of `NavController` or Android Context?
* [ ] Does the ViewModel handle Loading, Error, and Success states properly?

## General & Code Quality
* [ ] Are coroutines launched in the correct scope (e.g., `viewModelScope`)?
* [ ] Are Dispatchers injected rather than hardcoded (`Dispatchers.IO`)?
* [ ] Are the changes small and safe? (No unapproved wide-scale refactors).
* [ ] Have you verified the build and tests using [[build-run-test-guide]]?
