package com.mefy.platemate.presentation.components.model

/**
 * Visual style of [com.mefy.platemate.presentation.components.PMTextField].
 *
 * [Outlined] — bordered/labeled form field (default; used across auth/settings forms).
 * [Chat] — borderless pill for the message composer: no label/border, [surfaceVariant]
 * background, fully rounded shape, grows with content up to `maxLines`.
 */
enum class PMTextFieldVariant {
    Outlined,
    Chat,
    Search
}
