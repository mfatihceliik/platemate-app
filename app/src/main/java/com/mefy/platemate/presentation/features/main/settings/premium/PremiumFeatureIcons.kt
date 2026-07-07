package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Locale

/**
 * Resolves an admin-entered feature icon key to a Material icon. Unknown keys fall back to a check.
 * Keep this set in sync with the keys offered in the admin premium-features form.
 */
fun premiumFeatureIcon(key: String): ImageVector = when (key.lowercase(Locale.ROOT)) {
    "reviews" -> Icons.Filled.RateReview
    "adfree" -> Icons.Filled.Block
    "search" -> Icons.Filled.Search
    "badge" -> Icons.Filled.WorkspacePremium
    "alerts" -> Icons.Filled.Notifications
    "stats" -> Icons.Filled.BarChart
    "star" -> Icons.Filled.Star
    "bolt" -> Icons.Filled.Bolt
    "shield" -> Icons.Filled.Shield
    "infinity" -> Icons.Filled.AllInclusive
    else -> Icons.Filled.Check
}
