package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMEmptyState
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.features.main.messages.components.ConversationRow
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier,
    state: MessagesUiState,
    onAction: (MessagesUiAction) -> Unit,
    innerPadding: PaddingValues
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    val onMessageSearchResultClicked = remember(onAction) { { result: MessageSearchResultUiModel -> onAction(MessagesUiAction.MessageSearchResultClicked(result)) } }
    val onSearchQueryChanged = remember(onAction) {{ query: String -> onAction(MessagesUiAction.SearchQueryChanged(query)) }}
    val onConversationClicked = remember { { roomId: Long -> onAction(MessagesUiAction.ConversationClicked(roomId)) } }
    val onDeleteSwiped = remember { { roomId: Long -> onAction(MessagesUiAction.DeleteSwiped(roomId)) } }
    val onMarkReadSwiped = remember { { roomId: Long -> onAction(MessagesUiAction.MarkReadSwiped(roomId)) } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(vertical = spacing.s8)
    ) {
        item(contentType = "search") {
            PMSearchBar(
                query = state.searchQuery,
                onQueryChange = { onSearchQueryChanged(it) },
                placeholder = stringResource(R.string.messages_search_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(spacing.s8))
        }

        val hasConversationMatches = state.filteredConversations.isNotEmpty()
        val hasMessageMatches = state.messageSearchResults.isNotEmpty()

        if (state.searchQuery.isNotBlank() && !hasConversationMatches && !hasMessageMatches) {
            item {
                PMEmptyState(
                    icon = Icons.Outlined.SearchOff,
                    message = stringResource(R.string.messages_search_no_results),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        } else {
            items(
                items = state.filteredConversations, key = { it.roomId }
            ) { conversation ->
                ConversationRow(
                    conversation = conversation,
                    onClick = { onConversationClicked(conversation.roomId) },
                    onSwipeToDelete = { onDeleteSwiped(conversation.roomId) },
                    onSwipeToMarkRead = { onMarkReadSwiped(conversation.roomId) },
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.padding(start = dims.sizing.avatarMd + dims.spacing.s12),
                    color = colors.outlineVariant
                )
            }

            if (hasMessageMatches) {
                item(contentType = "section_label") {
                    PMSectionLabel(
                        text = stringResource(R.string.messages_search_section_messages),
                        modifier = Modifier.padding(
                            horizontal = spacing.s16,
                            vertical = spacing.s8
                        )
                    )
                }
                itemsIndexed(
                    items = state.messageSearchResults,
                    key = { _, result -> "msg_${result.messageId}" }
                ) { index, result ->

                    PMRowItem(
                        modifier = Modifier,
                        position = pmRowPositionOf(index, state.messageSearchResults.size),
                        onClick = { onMessageSearchResultClicked(result) },
                        title = result.participantName,
                        subtitle = result.content,
                        trailingText = result.time,
                        showChevron = true,

                    )
                }
            }
        }
    }
}

@Preview(name = "Messages Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations()
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}


@Preview(name = "Messages Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun MessagesDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations()
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}

@Preview(name = "Messages Search", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesSearchPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations(), searchQuery = "Ahmet"
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}

@Preview(name = "Messages Search With Message Matches", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesSearchWithMessageMatchesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false,
                conversations = previewConversations(),
                searchQuery = "plaka",
                messageSearchResults = previewMessageSearchResults()
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}

@Preview(name = "Messages Search No Results", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesSearchNoResultsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations(), searchQuery = "xyzxyz"
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}

private fun previewMessageSearchResults() = listOf(
    MessageSearchResultUiModel(
        messageId = 101,
        roomId = 3,
        participantName = "Mehmet C.",
        content = "34 ABC 123 plakalı araç önümde çok tehlikeli sollama yaptı",
        time = "14:12"
    ),
    MessageSearchResultUiModel(
        messageId = 102,
        roomId = 2,
        participantName = "Zeynep K.",
        content = "Plakayı gördüm, gerçekten nazik biri",
        time = "Dün"
    )
)

private fun previewConversations() = listOf(
    MessageConversationUiModel(
        roomId = 1,
        name = "Ahmet Y.",
        preview = "Teşekkürler, çok yardımcı oldun!",
        time = "09:24",
        unreadCount = 3,
        isSentByMe = false
    ), MessageConversationUiModel(
        roomId = 2,
        name = "Zeynep K.",
        preview = "Plakayı gördüm, gerçekten nazik biri",
        time = "Dün",
        unreadCount = 0,
        isSentByMe = true
    ), MessageConversationUiModel(
        roomId = 3,
        name = "Mehmet C.",
        preview = "Evet, o plakanın sahibiyim ben",
        time = "Pzt",
        unreadCount = 0,
        isSentByMe = false
    )
)
