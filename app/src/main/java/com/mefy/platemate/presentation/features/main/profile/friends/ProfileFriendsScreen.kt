package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun ProfileFriendsScreen(
    state: ProfileFriendsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val spacing = MaterialTheme.pmDimensions.spacing

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_friends_title),
            onBackClick = onBackClick
        )
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = spacing.s16, vertical = spacing.s8),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            if (state.friends.isEmpty()) {
            PMText(
                text = stringResource(R.string.profile_friends_empty),
                style = PMTextStyle.Body,
                color = colors.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                items(state.friends, key = { it.id }) { friend ->
                    PMCard(
                        modifier = Modifier.fillMaxWidth(),
                        padding = PaddingValues(horizontal = spacing.s12, vertical = spacing.s8)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
                            PMText(
                                text = friend.username,
                                style = PMTextStyle.Label,
                                color = colors.onSurface
                            )
                            PMText(
                                text = friend.status,
                                style = PMTextStyle.Caption,
                                color = colors.primary
                            )
                            PMText(
                                text = friend.createdAtText,
                                style = PMTextStyle.Caption,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
}
