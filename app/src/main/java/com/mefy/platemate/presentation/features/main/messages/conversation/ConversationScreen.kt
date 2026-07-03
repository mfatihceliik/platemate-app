package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DateSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReceivedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.UnreadMessagesSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SentBubble
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    state: ConversationUiState,
    onAction: (ConversationUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val listState = rememberLazyListState()

    // İlk açılışta okunmamış ayracına (yoksa en alta) SNAP; sonraki mesajlarda yalnızca
    // kullanıcı zaten dipteyse ya da mesaj kendisininse animasyonlu kaydır — geçmiş
    // okurken kullanıcıyı aşağı çekme (WhatsApp davranışı).
    var initialScrollDone by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(state.items.size, state.firstUnreadIndex) {
        if (state.items.isEmpty()) return@LaunchedEffect
        if (!initialScrollDone) {
            listState.scrollToItem(state.firstUnreadIndex ?: state.items.lastIndex)
            initialScrollDone = true
        } else {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val nearBottom = lastVisible >= state.items.lastIndex - 3
            val lastIsMine =
                (state.items.lastOrNull() as? ConversationListItem.Message)?.model?.isMine == true
            if (nearBottom || lastIsMine) {
                listState.animateScrollToItem(state.items.lastIndex)
            }
        }
    }

    // Klavye açılınca liste kökle birlikte daralır; son mesajlar klavyenin altında
    // kalmasın diye en alta kaydır.
    val imeVisible = WindowInsets.isImeVisible
    LaunchedEffect(imeVisible) {
        if (imeVisible && state.items.isNotEmpty()) {
            listState.scrollToItem(state.items.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(
            horizontal = dims.spacing.s12,
            vertical = dims.spacing.s8
        ),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        items(
            items = state.items,
            // Anahtar index İÇERMEZ: araya öğe girince (tarih başlığı, ayraç, yeni mesaj)
            // sonraki tüm öğelerin anahtarı bozulup item reuse iptal olmasın.
            key = { item ->
                when (item) {
                    is ConversationListItem.DateHeader -> "date_${item.label}"
                    is ConversationListItem.Message -> "msg_${item.model.id}"
                    is ConversationListItem.UnreadDivider -> "unread_divider"
                }
            },
            contentType = { item ->
                when (item) {
                    is ConversationListItem.DateHeader -> "date"
                    is ConversationListItem.Message -> "message"
                    is ConversationListItem.UnreadDivider -> "unread_divider"
                }
            }
        ) { item ->
            when (item) {
                is ConversationListItem.DateHeader ->
                    DateSeparator(label = item.label)

                is ConversationListItem.UnreadDivider ->
                    UnreadMessagesSeparator(count = item.count)

                is ConversationListItem.Message -> {
                    val msg = item.model
                    if (msg.isMine) {
                        SentBubble(
                            content = msg.content,
                            time = msg.time,
                            status = msg.status
                        )
                    } else {
                        ReceivedBubble(
                            initials = state.initials,
                            avatarBg = state.avatarBg,
                            avatarFg = state.avatarFg,
                            content = msg.content,
                            time = msg.time
                        )
                    }
                }
            }
        }
    }
}

private val previewState = ConversationUiState(
    isLoading = false,
    participantName = "Ahmet Yılmaz",
    initials = "AY",
    avatarBg = Color(0xFFECFEFF),
    avatarFg = Color(0xFF0E7490),
    inputText = "",
    items = listOf(
        ConversationListItem.DateHeader(label = "Bugün"),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 1L,
                content = "Merhaba, plakamı gördünüz mü",
                time = "10:42",
                isMine = false,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 2L,
                content = "Evet, 34 EK 0682 değil mi?",
                time = "10:43",
                isMine = true,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 3L,
                content = "Evet aynen o! Teşekkürler.",
                time = "10:44",
                isMine = false,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 4L,
                content = "Rica ederim, iyi günler!",
                time = "10:45",
                isMine = true,
                status = MessageStatus.SENT
            )
        ),
    )
)

@Preview(name = "ConversationScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ConversationScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ConversationScreen(
            state = previewState,
            onAction = {},
        )
    }
}

@Preview(name = "ConversationScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ConversationScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ConversationScreen(
            state = previewState,
            onAction = {},
        )
    }
}
