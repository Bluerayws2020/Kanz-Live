package com.blueray.fares.videoliveeventsample.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityLiveEventSetUpBinding
import com.sendbird.live.LiveEvent
import com.sendbird.live.MediaOptions
import com.sendbird.live.SendbirdLive
import com.blueray.fares.videoliveeventsample.model.TextBottomSheetDialogItem
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.audioNameResId
import com.blueray.fares.videoliveeventsample.util.cameraNameResId
import com.blueray.fares.videoliveeventsample.util.getAvailableAudioDevice
import com.blueray.fares.videoliveeventsample.util.getAvailableVideoDevices
import com.blueray.fares.videoliveeventsample.util.showPermissionDenyDialog
import com.blueray.fares.videoliveeventsample.util.showSheetRadioDialog
import com.blueray.fares.videoliveeventsample.util.showToast
import com.sendbird.webrtc.AudioDevice
import com.sendbird.webrtc.VideoDevice

class LiveEventSetUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventSetUpBinding
    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN

        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value }) {
            initCameraPreview()
        } else {
            Log.e("[SendbirdLiveUIKit]", "permission denied ${it.filter { map -> !map.value }.keys.joinToString(", ")}")
            showPermissionDenyDialog(true, it.filter { permission -> !permission.value }.keys.toList())
        }
    }

    private var liveEvent: LiveEvent? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var audioManager: AudioManager
    private var currentVideoDevice: VideoDevice? = null
    private val availableVideoDevices = mutableListOf<VideoDevice>()
    private var currentAudioDevice: AudioDevice? = null
    private val availableAudioDevices: Array<AudioDevice>
        get() = if (::audioManager.isInitialized) audioManager.getAvailableAudioDevice(this) else emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveEventSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher.launch(permissions)
        val liveEventId = intent.getStringExtra(INTENT_KEY_LIVE_EVENT_ID)
        if (liveEventId.isNullOrBlank()) {
            showToast(R.string.error_message_error_description)
            finish()
            return
        }
        liveEvent = SendbirdLive.getCachedLiveEvent(liveEventId)
        if (liveEvent == null) {
            showToast(R.string.error_message_error_description)
            finish()
            return
        }
        initView()
    }

    private fun initView() {
        binding.tvEnter.setOnClickListener { enterTheLiveEvent() }
        binding.ivFlip.setOnClickListener { flipCamera() }
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun enterTheLiveEvent() {
        val liveEvent = liveEvent
        if (liveEvent == null) {
            Log.e("[SendbirdLiveSample]", "enterAsHost() liveEvent == null")
            showToast(R.string.error_message_error_description)
            finish()
            return
        }
        liveEvent.enterAsHost(MediaOptions(videoDevice = currentVideoDevice, audioDevice = currentAudioDevice)) { e ->
            if (e != null) {
                Log.e("[SendbirdLiveSample]", "enterAsHost() e: $e")
                showToast(e.message ?: "")
                return@enterAsHost
            }
            Log.v("[SendbirdLiveSample]", "enterAsHost() succeed")
            val intent = Intent(this, LiveEventForHostActivity::class.java)
            intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
            startActivity(intent)
            finish()
        }
    }

    private fun flipCamera() {
        val videoDevice = if (currentVideoDevice?.position == VideoDevice.Position.FRONT) {
            availableVideoDevices.filter { it.position == VideoDevice.Position.BACK }.getOrNull(0)
        } else {
            availableVideoDevices.filter { it.position == VideoDevice.Position.FRONT }.getOrNull(0)
        }
        if (videoDevice == null) {
            showToast(R.string.error_message_error_description)
            return
        }
        setVideoDevice(videoDevice)
    }

    private fun initCameraPreview() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        availableVideoDevices.addAll(cameraManager.getAvailableVideoDevices(this))
        val currentVideoDevice = availableVideoDevices.filter { it.position == VideoDevice.Position.FRONT }.getOrElse(0) { availableVideoDevices.firstOrNull() }
        if (currentVideoDevice == null) {
            showToast(R.string.error_message_error_description)
            finish()
            return
        }
        val currentAudioDevice = if (availableAudioDevices.contains(AudioDevice.BLUETOOTH)) AudioDevice.BLUETOOTH else if (availableAudioDevices.contains(AudioDevice.SPEAKERPHONE)) AudioDevice.SPEAKERPHONE else availableAudioDevices[0]
        setAudioDevice(currentAudioDevice)
        binding.cpLiveEvent.initLayout(cameraManager, currentVideoDevice,
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                    setVideoDevice(currentVideoDevice)
                }

                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
                }

                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                }
            })
        if (binding.cpLiveEvent.isAvailable) {
            setVideoDevice(currentVideoDevice)
        }
        binding.sivCameraDevice.setOnClickListener {
            val availableCameraDialogItems = availableVideoDevices.map { TextBottomSheetDialogItem(it.deviceName.toInt(), it.cameraNameResId(), R.style.Text16OnDark01) }
            showSheetRadioDialog(
                title = getString(R.string.camera),
                titleAppearance = R.style.AppTheme,
                backgroundDrawableRes = R.drawable.shape_sheet_dialog_background,
                items = availableCameraDialogItems,
                checkedItemPosition = availableVideoDevices.indexOf(this.currentVideoDevice)
            ) { position, dialogItem ->
                if (dialogItem != null) {
                    setVideoDevice(availableVideoDevices[position])
                }
            }
        }
        binding.sivAudioDevice.setOnClickListener {
            val availableAudioDialogItems = availableAudioDevices.mapIndexed { index, audioDevice -> TextBottomSheetDialogItem(index, audioDevice.audioNameResId(), R.style.Text16OnDark01) }
            showSheetRadioDialog(
                title = getString(R.string.audio_device),
                titleAppearance = R.style.Text18OnDark01,
                backgroundDrawableRes = R.drawable.shape_sheet_dialog_background,
                items = availableAudioDialogItems,
                checkedItemPosition = availableAudioDevices.indexOf(this.currentAudioDevice)
            ) { position, dialogItem ->
                if (dialogItem != null) {
                    setAudioDevice(availableAudioDevices[position])
                }
            }
        }
    }

    private fun setVideoDevice(videoDevice: VideoDevice) {
        currentVideoDevice = videoDevice
        binding.cpLiveEvent.switchCameraByCameraId(videoDevice.deviceName)
        binding.sivCameraDevice.setTag(getString(videoDevice.cameraNameResId()))
    }

    private fun setAudioDevice(audioDevice: AudioDevice) {
        currentAudioDevice = audioDevice
        binding.sivAudioDevice.setTag(audioDevice.name)
    }

}