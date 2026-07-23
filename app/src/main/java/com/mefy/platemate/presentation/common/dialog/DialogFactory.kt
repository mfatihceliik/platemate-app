package com.mefy.platemate.presentation.common.dialog

import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText

/**
 * Tek noktadan ortak dialog kurucu. Hata/oturum-bitti gibi tekrar eden dialog'lar
 * burada üretilir; böylece [com.mefy.platemate.presentation.navigation.AppNavHost] ve
 * [com.mefy.platemate.presentation.common.viewmodel.BaseViewModel] aynı kaynağı kullanır.
 */
object DialogFactory {

    /** Oturum süresi doldu; onaylanınca yeniden giriş akışına yönlendirir. */
    fun sessionExpiredDialog(onConfirm: () -> Unit): DialogModel = DialogModel(
        title = UiText.Resource(R.string.session_expired_title),
        message = UiText.Resource(R.string.common_error_unauthorized),
        confirmText = UiText.Resource(R.string.session_expired_action_login),
        dismissible = false,
        variant = DialogVariant.Error,
        onConfirm = onConfirm
    )

    /** Chat silme onay dialog'u. */
    fun deleteChatConfirmDialog(onConfirm: () -> Unit): DialogModel = DialogModel(
        title = UiText.Resource(R.string.messages_delete_confirm_title),
        message = UiText.Resource(R.string.messages_delete_confirm_message),
        confirmText = UiText.Resource(R.string.common_delete),
        dismissText = UiText.Resource(R.string.common_cancel),
        dismissible = true,
        variant = DialogVariant.Error,
        onConfirm = onConfirm
    )

    /** Kamera izni kalıcı reddedilmiş (shouldShowRequestPermissionRationale=false); sistem
     * dialogu bir daha gösterilemez, kullanıcı Ayarlar'a yönlendirilir. */
    fun cameraPermissionDeniedDialog(onOpenSettings: () -> Unit, onDismiss: () -> Unit): DialogModel = DialogModel(
        title = UiText.Resource(R.string.camera_permission_denied_title),
        message = UiText.Resource(R.string.camera_permission_denied_message),
        confirmText = UiText.Resource(R.string.camera_permission_open_settings),
        dismissText = UiText.Resource(R.string.common_cancel),
        dismissible = true,
        variant = DialogVariant.Info,
        onConfirm = onOpenSettings,
        onDismiss = onDismiss
    )
}
