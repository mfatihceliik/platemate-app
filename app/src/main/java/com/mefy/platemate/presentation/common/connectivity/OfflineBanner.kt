package com.mefy.platemate.presentation.common.connectivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.pmColors

/**
 * Bağlantı kesildiğinde içeriğin üstünde beliren kalıcı ince şerit.
 *
 * Bloklamaz (pasif durum bilgisi); [visible] true olduğu sürece görünür kalır,
 * bağlantı gelince animasyonla kaybolur. Kritik bir işlem sırasında oluşan
 * hata pop-up'ından ([GlobalAppEvent.ShowGlobalDialog]) farklıdır.
 */
@Composable
fun OfflineBanner(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.pmColors.error)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                contentDescription = null,
                tint = MaterialTheme.pmColors.onError
            )
            Text(
                text = stringResource(R.string.connectivity_offline_banner),
                color = MaterialTheme.pmColors.onError,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
