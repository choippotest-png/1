package ua.alertnotifier.app

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes

enum class AlertType(
    val notificationId: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    @RawRes val soundRes: Int,
    @DrawableRes val iconRes: Int,
    val spokenAnnouncement: String? = null
) {
    AIR_RAID(
        notificationId = 1001,
        titleRes = R.string.alert_air_raid_title,
        messageRes = R.string.alert_air_raid_message,
        soundRes = R.raw.air_raid_alert,
        iconRes = R.drawable.ic_alert_siren
    ),
    ELEVATED(
        notificationId = 1002,
        titleRes = R.string.alert_elevated_title,
        messageRes = R.string.alert_elevated_message,
        soundRes = R.raw.elevated_alert,
        iconRes = R.drawable.ic_alert_elevated
    ),
    ALL_CLEAR(
        notificationId = 1003,
        titleRes = R.string.alert_all_clear_title,
        messageRes = R.string.alert_all_clear_message,
        soundRes = R.raw.all_clear_alert,
        iconRes = R.drawable.ic_alert_clear
    ),
    MINUTE_OF_SILENCE(
        notificationId = 1004,
        titleRes = R.string.alert_minute_silence_title,
        messageRes = R.string.alert_minute_silence_message,
        soundRes = R.raw.minute_of_silence_metronome,
        iconRes = R.drawable.ic_minute_silence,
        spokenAnnouncement = "Увага, загальнонаціональна хвилина мовчання."
    )
}
