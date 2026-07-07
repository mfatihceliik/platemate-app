package com.mefy.platemate.presentation.components.model

/**
 * Visual weight of a [com.mefy.platemate.presentation.components.PMChip].
 *
 * All three styles derive their colors from a single `accentColor`, so a chip
 * is never tied to a hardcoded brand color — the accent can come from the theme
 * or from a backend-provided hex.
 *
 * - [Soft]    — alpha-tinted fill + subtle border. Default display badge look.
 * - [Solid]   — filled with the accent, auto-contrast text. Also the `selected` look.
 * - [Outline] — transparent fill + accent border. Unselected selectable-chip look.
 */
enum class PMChipStyle { Soft, Solid, Outline }
