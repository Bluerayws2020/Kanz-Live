package com.blueray.fares.videoliveeventsample.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import coil.load
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityCreateLiveEventBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.live.LiveEventCreateParams
import com.sendbird.live.SendbirdLive
import com.blueray.fares.videoliveeventsample.model.SelectedHostUser
import com.blueray.fares.videoliveeventsample.model.TextBottomSheetDialogItem
import com.blueray.fares.videoliveeventsample.util.FileUtil
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_CREATED_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_SELECTED_HOST_USER_LIST
import com.blueray.fares.videoliveeventsample.util.areAnyPermissionsGranted
import com.blueray.fares.videoliveeventsample.util.cameraIntent
import com.blueray.fares.videoliveeventsample.util.hasIntent
import com.blueray.fares.videoliveeventsample.util.imageGalleryIntent
import com.blueray.fares.videoliveeventsample.util.showAlertDialog
import com.blueray.fares.videoliveeventsample.util.showBottomSheetDialog
import com.blueray.fares.videoliveeventsample.util.showPermissionDenyDialog
import com.blueray.fares.videoliveeventsample.util.showToast
import com.blueray.fares.videoliveeventsample.util.signOut
import com.sendbird.live.LiveEvent
import java.io.File
import java.util.Collections.addAll

const val DIALOG_ITEM_ID_PHOTO_LIBRARY = 0
const val DIALOG_ITEM_ID_TAKE_PHOTO = 1
const val DIALOG_ITEM_ID_REMOVE_PHOTO = 2

class CreateLiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateLiveEventBinding
    private var selectedHostUsers: MutableMap<String, SelectedHostUser> = mutableMapOf()
    private var selectedPhotoUri: Uri? = null
    private var pendingPhotoUri: Uri? = null
    private val cameraPermission = listOf(
        Manifest.permission.CAMERA
    )
    private val isCameraPermissionGranted: Boolean
        get() = areAnyPermissionsGranted(cameraPermission.toTypedArray())
    private val getContentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        SendbirdChat.autoBackgroundDetection = true
        val intent = result.data
        val resultCode = result.resultCode
        if (resultCode != Activity.RESULT_OK || intent == null) return@registerForActivityResult
        val mediaUri = intent.data
        if (mediaUri != null) {
            selectedPhotoUri = mediaUri
            binding.ivCover.load(mediaUri)
        }
    }

    private val takeCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        SendbirdChat.autoBackgroundDetection = true
        val resultCode = result.resultCode
        val mediaUri = pendingPhotoUri.also { pendingPhotoUri = null }
        if (resultCode != Activity.RESULT_OK || mediaUri == null) return@registerForActivityResult
        selectedPhotoUri = mediaUri
        binding.ivCover.load(mediaUri)
    }
    private val selectHostResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedHostUsers = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableArrayListExtra(INTENT_KEY_SELECTED_HOST_USER_LIST, SelectedHostUser::class.java)
            } else {
                result.data?.getParcelableArrayListExtra(INTENT_KEY_SELECTED_HOST_USER_LIST)
            }?.toMutableList() ?: return@registerForActivityResult
            this.selectedHostUsers.clear()
            this.selectedHostUsers.putAll(selectedHostUsers.map { it.userId to it })
            binding.tvCreateLiveEventHostCount.text = this.selectedHostUsers.size.toString()
        }
    }
    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value }) {
                showMediaSelectDialog()
            } else {
                showPermissionDenyDialog(deniedList = it.filter { permissions -> !permissions.value }.keys.toList())
            }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLiveEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        initView()
    }

    private fun initView() {
        val currentUser = SendbirdLive.currentUser
        if (currentUser == null) {
            signOut()
            return
        }
        addHostIds(listOf(SelectedHostUser(currentUser.userId, currentUser.nickname, currentUser.profileUrl)))
        binding.ivBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.ivCover.setOnClickListener {
            showMediaSelectDialog()
        }
        binding.tvCreate.setOnClickListener {
            val title = binding.etCreateLiveEventTitle.text.toString()
            val userIdsForHost = selectedHostUsers.map { it.value.userId }
            val file = selectedPhotoUri?.let { FileUtil().uriToFile(this, it) }
            createLiveEvent(title, userIdsForHost, file)
        }
        binding.clUserIdsForHost.setOnClickListener {
            val intent = Intent(this, UserIdsForHostListActivity::class.java).apply {
                putParcelableArrayListExtra(INTENT_KEY_SELECTED_HOST_USER_LIST, ArrayList(selectedHostUsers.map { it.value }))
            }
            selectHostResult.launch(intent)
        }
    }

    private fun createLiveEvent(title: String, userIdsForHost: List<String>, coverFile: File?) {
        val params = LiveEventCreateParams(userIdsForHost).apply {
            this.title = title
            if (coverFile != null) this.coverFile = coverFile
//            type = LiveEventType.LIVE_EVENT_FOR_VIDEO
        }
        SendbirdLive.createLiveEvent(params) createLiveEventLabel@ { liveEvent, e ->
            if (e != null || liveEvent == null) {
                showToast(e?.message ?: "")
                return@createLiveEventLabel
            }
            enterAsHost(liveEvent)
        }
    }
    private fun enterAsHost(liveEvent: LiveEvent) {
//        val requestPermissions = permissions.toMutableList().apply {
//            addAll(
//                arrayOf(
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_PHONE_STATE
//                )
//            )
//        }.toTypedArray()
//        if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
//            requestPermissionLauncher.launch(requestPermissions)
//            return
//        }

        val intent = Intent(this, LiveEventSetUpActivity::class.java)
        intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)


        startActivity(intent)

        finish()

    }
    private fun addHostIds(hostIds: List<SelectedHostUser>) {
        selectedHostUsers.putAll(hostIds.map { it.userId to it })
        binding.tvCreateLiveEventHostCount.text = "${selectedHostUsers.size}"
    }

    private fun showMediaSelectDialog() {
        val photoLibraryItem = TextBottomSheetDialogItem(
            DIALOG_ITEM_ID_PHOTO_LIBRARY,
            R.string.photo_library,
            R.style.Text16OnDark01
        )

        val takePhotoItem = TextBottomSheetDialogItem(
            DIALOG_ITEM_ID_TAKE_PHOTO,
            R.string.take_photo,
            R.style.Text16OnDark01
        )

        val items = mutableListOf(photoLibraryItem, takePhotoItem)

        if (selectedPhotoUri != null) {
            val removePhotoItem = TextBottomSheetDialogItem(
                DIALOG_ITEM_ID_REMOVE_PHOTO,
                R.string.remove_photo,
                R.style.Text16Error200
            )

            items.add(0, removePhotoItem)
        }

        showBottomSheetDialog(
            items,
            backgroundColor = R.color.background_500
        ) { _, _, data ->
            when (data.id) {
                DIALOG_ITEM_ID_PHOTO_LIBRARY -> getPhotoFromGallery()
                DIALOG_ITEM_ID_TAKE_PHOTO -> takePhoto()
                DIALOG_ITEM_ID_REMOVE_PHOTO -> {
                    selectedPhotoUri = null
                    binding.ivCover.load(null)
                }
            }
        }
    }

    private fun getPhotoFromGallery() {
        SendbirdChat.autoBackgroundDetection = false
        val intent = imageGalleryIntent()
        getContentLauncher.launch(intent)
    }

    private fun takePhoto() {
        if (!isCameraPermissionGranted) {
            requestCameraPermission()
            return
        }
        SendbirdChat.autoBackgroundDetection = false
        val uri = FileUtil().createImageFileUri(this) ?: return
        val intent = cameraIntent(uri)
        if (hasIntent(intent)) {
            pendingPhotoUri = uri
            takeCameraLauncher.launch(intent)
        }
    }

    private fun requestCameraPermission() {
        val permissions = cameraPermission.toTypedArray()
        if (!areAnyPermissionsGranted(permissions)) {
            showAlertDialog(
                getString(R.string.permission_dialog_title),
                getString(R.string.permission_dialog_message_for_camera),
                getString(R.string.permission_dialog_apply),
                getString(R.string.permission_dialog_deny),
                positiveButtonFunction = { this.permissionLauncher.launch(permissions) },
                negativeButtonFunction = { showPermissionDenyDialog(deniedList = permissions.filter { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) }
            )
            return
        }
        this.permissionLauncher.launch(permissions)
    }
}