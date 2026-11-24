package ua.alertnotifier.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import ua.alertnotifier.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationHelper: AlertNotificationHelper
    private lateinit var audioManager: AlertAudioManager

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationHelper = AlertNotificationHelper(this)
        audioManager = AlertAudioManager(this) { activeAlert ->
            updateButtonStates(activeAlert)
        }

        askNotificationPermissionIfNeeded()
        setupButtons()
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupButtons() {
        binding.btnAirRaid.setOnClickListener { handleAlertClick(AlertType.AIR_RAID) }
        binding.btnElevated.setOnClickListener { handleAlertClick(AlertType.ELEVATED) }
        binding.btnAllClear.setOnClickListener { handleAlertClick(AlertType.ALL_CLEAR) }
        binding.btnMinuteSilence.setOnClickListener { handleAlertClick(AlertType.MINUTE_OF_SILENCE) }
    }

    private fun handleAlertClick(alertType: AlertType) {
        val wasSame = audioManager.currentAlert == alertType
        audioManager.triggerAlert(alertType)
        if (wasSame) {
            notificationHelper.clearNotifications()
        } else {
            notificationHelper.showAlertNotification(alertType)
        }
    }

    private fun updateButtonStates(activeAlert: AlertType?) {
        updateSingleButton(
            binding.btnAirRaid,
            R.string.btn_air_raid,
            activeAlert == AlertType.AIR_RAID
        )
        updateSingleButton(
            binding.btnElevated,
            R.string.btn_elevated,
            activeAlert == AlertType.ELEVATED
        )
        updateSingleButton(
            binding.btnAllClear,
            R.string.btn_all_clear,
            activeAlert == AlertType.ALL_CLEAR
        )
        updateSingleButton(
            binding.btnMinuteSilence,
            R.string.btn_minute_silence,
            activeAlert == AlertType.MINUTE_OF_SILENCE
        )
    }

    private fun updateSingleButton(button: MaterialButton, baseLabelRes: Int, isActive: Boolean) {
        val baseLabel = getString(baseLabelRes)
        button.text = if (isActive) {
            getString(R.string.btn_active_format, baseLabel)
        } else {
            baseLabel
        }
        button.alpha = if (isActive) 1.0f else 0.92f
    }

    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }
}
