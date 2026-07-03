package com.mefy.platemate.core.notification.di

import com.mefy.platemate.core.notification.presenter.FriendRequestNotificationPresenter
import com.mefy.platemate.core.notification.presenter.MessageNotificationPresenter
import com.mefy.platemate.core.notification.presenter.NewFollowerNotificationPresenter
import com.mefy.platemate.core.notification.presenter.NotificationPresenter
import com.mefy.platemate.core.notification.presenter.PlateReviewNotificationPresenter
import com.mefy.platemate.core.notification.presenter.SystemNotificationPresenter
import com.mefy.platemate.domain.model.notification.NotificationType
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

/** [NotificationType] anahtarlı presenter çoklu-bind'i için map key. */
@MapKey
annotation class NotificationTypeKey(val value: NotificationType)

/** Tip → [NotificationPresenter] eşlemesini Hilt multibinding ile kurar (open-closed). */
@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @IntoMap
    @NotificationTypeKey(NotificationType.MESSAGE)
    abstract fun bindMessagePresenter(impl: MessageNotificationPresenter): NotificationPresenter

    @Binds
    @IntoMap
    @NotificationTypeKey(NotificationType.FRIEND_REQUEST)
    abstract fun bindFriendRequestPresenter(impl: FriendRequestNotificationPresenter): NotificationPresenter

    @Binds
    @IntoMap
    @NotificationTypeKey(NotificationType.PLATE_REVIEW)
    abstract fun bindPlateReviewPresenter(impl: PlateReviewNotificationPresenter): NotificationPresenter

    @Binds
    @IntoMap
    @NotificationTypeKey(NotificationType.NEW_FOLLOWER)
    abstract fun bindNewFollowerPresenter(impl: NewFollowerNotificationPresenter): NotificationPresenter

    @Binds
    @IntoMap
    @NotificationTypeKey(NotificationType.SYSTEM)
    abstract fun bindSystemPresenter(impl: SystemNotificationPresenter): NotificationPresenter
}
