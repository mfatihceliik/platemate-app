# Design System Guidelines

PlateMate relies on a strict internal Compose Design System to maintain a premium look.

## Core Rules
* **No Hardcoded Values:** Never use raw `dp` or `sp` values directly in standard screens.
* **No Hardcoded Strings:** Always use `stringResource(R.string.*)`.
* **Modifier Positioning:** `modifier: Modifier = Modifier` must always be the first optional parameter in any public Composable.

## Hardcoded Value Exceptions

Avoid hardcoded `dp` and `sp` values in standard screens.

Allowed exceptions:
- `0.dp`
- `1.dp` for dividers or borders if no token exists
- one-off animation offsets when documented locally
- values inside Design System token definitions
- temporary values during prototyping, but they must not remain in final code

If the same value appears in more than one place, promote it to a design token.

## Design Tokens
Access tokens through the defined theme objects:
* **Colors:** Use `PMSemanticColors` or `MaterialTheme.colorScheme` (avoid hardcoded Hex or standard `Color.Red`).
* **Spacing & Radius:** Use `PMDimensions` (e.g., `PMDimensions.paddingMedium`, `PMDimensions.radiusLarge`).
* **Typography:** Use `MaterialTheme.typography` (e.g., `MaterialTheme.typography.bodyLarge`).

## Reusable Components
Before building a generic UI piece (Button, TextField, Card, TopBar), check if a `PM...` component already exists. If one does not exist and it will be used in multiple places, create it as a stateless `PM[Type]` component.

## Preview Rules
* **Mandatory:** All Screen-level composables (`*Screen`) and reusable Design System components (`PM*`) MUST have a `@Preview`.
* **Optional:** Small, private helper composables within a single screen file do not require previews.
* **No ViewModels in Previews:** Never pass a ViewModel into a preview. Previews only accept `UiState` and mock lambda callbacks.
