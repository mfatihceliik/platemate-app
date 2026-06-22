package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions


/** Yüzen pill barın (Row) iç yüksekliği: BarHeight + 2*s12. Nav inset hariç. */
/**
 * PlateMate'e özgü yüzen-hap (floating pill) alt navigasyon barı.
 *
 * - Ekran kenarlarından boşluklu, tam yuvarlatılmış, gölgeli yüzen bar.
 * - Ortadaki [TopLevelDestination.Messages] sekmesi, gradient + ışıltılı gölgeli
 *   yükseltilmiş bir FAB olarak barın üstünden taşar.
 * - Seçili sekme `primaryContainer` hap + etiket gösterir; diğerleri yalnızca ikon.
 *
 * Erişilebilirlik/test: her sekme [selectable] (Role.Tab) ve ikonu her durumda
 * `contentDescription = label` taşır.
 */
@Composable
fun MainBottomBar(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val pillShape = RoundedCornerShape(dims.radius.rFull)

    // Box yalnız bar (Row) yüksekliğini kaplar; bottombar slotu fazladan yer
    // ayırmaz. FAB negatif offset ile içeriğin üstüne taşar (slot yüksekliğini
    // büyütmez), böylece scroll'lu ekranda pill üstünde ölü bant kalmaz.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1) Yüzen hap bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
                .height(dims.sizing.mainBottomBarContentHeight)
                .shadow(
                    elevation = dims.spacing.s16,
                    shape = pillShape,
                    spotColor = colors.cardShadow,
                    ambientColor = colors.cardShadow
                )
                .clip(pillShape)
                .background(colors.surface)
                .border(dims.stroke.st1, colors.outlineVariant, pillShape)
                .padding(horizontal = dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopLevelDestination.entries.forEach { destination ->
                if (destination == TopLevelDestination.Messages) {
                    // Orta yuvayı rezerve et; FAB overlay olarak üstte çizilir.
                    Spacer(Modifier.weight(1f))
                } else {
                    PMBottomBarItem(
                        destination = destination,
                        selected = destination == selectedDestination,
                        onClick = { onDestinationSelected(destination) }
                    )
                }
            }
        }

        // 2) Orta (Messages) butonu — diğerleriyle aynı hizada, yalnız s8 yukarıda
        CenterFab(
            selected = selectedDestination == TopLevelDestination.Messages,
            onClick = { onDestinationSelected(TopLevelDestination.Messages) },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -dims.spacing.s8)
        )
    }
}


@Preview(name = "MainBottomBar Light", showBackground = true)
@Composable
private fun MainBottomBarLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MainBottomBar(
            selectedDestination = TopLevelDestination.Search,
            onDestinationSelected = {}
        )
    }
}

@Preview(name = "MainBottomBar Dark", showBackground = true)
@Composable
private fun MainBottomBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MainBottomBar(
            selectedDestination = TopLevelDestination.Messages,
            onDestinationSelected = {}
        )
    }
}
