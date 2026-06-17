---
type: android-doc
area: navigation
tags:
  - android
  - navigation-compose
  - compose
aliases:
  - Android Navigation
  - Navigation Compose
updated: 2026-05-29
---

# Navigation

## Purpose

Documents Navigation Compose conventions for this project.

## Current Pattern

The app uses typed serializable destinations implementing `AppDestination`:

```kotlin
@Serializable
data object SearchDestination : AppDestination

@Serializable
data class SearchDetailDestination(val id: String) : AppDestination
```

Graphs are grouped by area:

- session gate graph
- auth graph
- main graph
- search graph
- discover graph
- messages graph
- profile graph

## Navigation Ownership

- ViewModels emit `UiEffect` for navigation intent.
- Routes collect effects and call navigation callbacks.
- Graph/AppState/NavController performs actual navigation.
- Screens never receive `NavController` directly.

Related docs: [[feature-patterns]], [[compose-ui]].

## Destination Rules

- Add a destination in `presentation/navigation/destinations`.
- Use `data object` for destinations without args.
- Use `data class` for destinations with args.
- Keep route arguments small and serializable.
- Do not pass full domain/UI objects through navigation; pass id/code and reload/resolve as needed.

## Graph Rules

- Add destinations to the feature graph, not directly to root unless root-level.
- Keep graph functions internal when not used outside navigation package.
- Use parent graph scoped ViewModels only when state must be shared across child destinations.
- Keep bottom bar behavior in `AppState` / top-level destination mapping.

## Bottom Bar Rules

Bottom bar should show only on top-level start destinations.

Do not show bottom bar on detail/settings/chat/edit screens unless explicitly required.

## Session/Auth Rules

- Session gate decides initial auth/main route.
- Global session invalidation may navigate back to auth.
- Avoid duplicate auth redirects by checking whether current destination is already inside auth graph.
- Use lifecycle-aware collection for root auth/session state.

## Deep Link Rules

The code may include placeholders for future deep links. When implementing deep links:

- use typed destinations where possible
- document expected URI shape
- validate required args
- avoid exposing sensitive tokens or personal data in deep links

## Common Mistakes to Avoid

- passing `NavController` into a Screen
- storing navigation commands as persistent state
- passing entire domain objects as route args
- duplicating graph logic across multiple files
- showing bottom bar on unintended detail screens
