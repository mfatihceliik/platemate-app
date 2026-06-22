package com.mefy.platemate.presentation.features.main.platedetail.review

sealed interface ReviewUiAction {
    data object BackClicked : ReviewUiAction
    data class OverallRatingChanged(val rating: Int) : ReviewUiAction
    data class TagToggled(val code: String) : ReviewUiAction
    data class CommentChanged(val text: String) : ReviewUiAction
    data class AnonymousToggled(val checked: Boolean) : ReviewUiAction
    data object SubmitClicked : ReviewUiAction

    // Sonuç popup aksiyonları
    data object GoHomeClicked : ReviewUiAction        // "Ana Sayfaya Git" → geri dön
    data object RateAnotherClicked : ReviewUiAction   // "Başka Araç Puanla" → popup kapat + formu sıfırla
    data object RetrySubmitClicked : ReviewUiAction   // "Tekrar Dene" → yeniden gönder
    data object DismissResult : ReviewUiAction        // "İptal Et" → popup kapat
}
