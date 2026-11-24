package ua.alertnotifier.app

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class AlertAudioManager(
    private val context: Context,
    private val onStateChanged: (AlertType?) -> Unit
) : TextToSpeech.OnInitListener {

    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isTtsReady = false
    private val mainHandler = Handler(Looper.getMainLooper())

    var currentAlert: AlertType? = null
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val ukLocale = Locale("uk", "UA")
            val result = tts?.setLanguage(ukLocale)
            isTtsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
            tts?.setSpeechRate(0.95f)
        } else {
            isTtsReady = false
        }
    }

    fun triggerAlert(alertType: AlertType) {
        // Tapping the already playing button stops it
        if (currentAlert == alertType) {
            stopAll()
            return
        }

        stopAll(notify = false)
        currentAlert = alertType
        onStateChanged(alertType)

        val announcement = alertType.spokenAnnouncement
        if (!announcement.isNullOrBlank() && isTtsReady && tts != null) {
            val utteranceId = "utterance_${alertType.name}_${System.currentTimeMillis()}"
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(id: String?) {}

                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        mainHandler.post {
                            if (currentAlert == alertType) {
                                playRawSound(alertType)
                            }
                        }
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(id: String?) {
                    if (id == utteranceId) {
                        mainHandler.post {
                            if (currentAlert == alertType) {
                                playRawSound(alertType)
                            }
                        }
                    }
                }
            })
            tts?.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } else {
            playRawSound(alertType)
        }
    }

    private fun playRawSound(alertType: AlertType) {
        releasePlayer()
        val afd = context.resources.openRawResourceFd(alertType.soundRes) ?: return
        val player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            setOnCompletionListener {
                stopAll()
            }
            prepare()
            start()
        }
        mediaPlayer = player
    }

    fun stopAll(notify: Boolean = true) {
        tts?.stop()
        releasePlayer()
        currentAlert = null
        if (notify) {
            onStateChanged(null)
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
            } catch (_: IllegalStateException) {
            }
            it.release()
        }
        mediaPlayer = null
    }

    fun release() {
        stopAll(notify = false)
        tts?.shutdown()
        tts = null
    }
}
