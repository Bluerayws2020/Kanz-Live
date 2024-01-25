package com.blueray.Kanz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.abedelazizshe.lightcompressorlibrary.config.SaveLocation
import com.abedelazizshe.lightcompressorlibrary.config.SharedStorageConfiguration
import com.blueray.Kanz.R
import com.blueray.Kanz.adapters.ActivitiesTypesAdapterForUploade
import com.blueray.Kanz.api.OnCategroryChose
import com.blueray.Kanz.databinding.ActivityUploadeVedioBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.HelperUtils.showMessage
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.fragments.getFileSize
import com.blueray.Kanz.ui.viewModels.AppViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class UploadeVedio : AppCompatActivity() {
    private lateinit var binding: ActivityUploadeVedioBinding
    private val viewmodel by viewModels<AppViewModel>()
    private val VIDEO_REQUEST_CODE = 3
    private val IMAGE_REQUEST_CODE = 4
    private lateinit var adapter: ActivitiesTypesAdapterForUploade
    var categoryId = ""
    var viemoLink = ""
    private var image_Video = true
    private val REQUEST_CODE = 100
    private val NotfiCode = 101

    private var videoUri: Uri? = null
    private val uris = mutableListOf<Uri>()
    private val client = OkHttpClient()

    var playableVideoPath: String? = ""
    lateinit var uri: Uri
    var newSize: String = ""
    var progressPercent: Float = 0F
    var isCompressing = false

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (HelperUtils.getUid(this@UploadeVedio) == "0") {
                Toast.makeText(this, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                if (isGranted) {
                    launchCamera()
                } else {
                    // Permission has been denied, handle it here
                    Toast.makeText(this, "يجب اعطاء اذن الكاميرا", Toast.LENGTH_SHORT).show()
                    // Optionally, disable features that depend on this permission
                }
            }
        }

    // Handle the result of the camera activity
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                // The Intent contains the URI of the video
//                videoUri = result.data?.data
//                prepareVideoUpload(videoUri!!)
                videoUri = result.data?.data
                processVideo()
                displayVideoThumbnail(videoUri)
            } else {
//            startActivity(Intent(this,HomeActivity::class.java))
            }
        }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadeVedioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize your views
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.includeTap.title.text = "تحميل الفيديو"
        binding.includeTap.title.setTextColor(R.color.green)
        

        binding.includeTap.back.setOnClickListener {

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        if (HelperUtils.getUid(this@UploadeVedio) == "0") {
            Toast.makeText(this, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NotfiCode
                )
            }
        }

        resetProgress()

        binding.uploades.playAnimation()
        binding.uploades.setOnClickListener {

            showUploadOptions()

        }
        // Set the click listener for the record button
        // Check if we have permission to record


//        // Set the click listener for the upload button
//        binding.btnUploadVideo.setOnClickListener {
//            videoUri?.let {
//                binding.progressBar.visibility = View.VISIBLE
//                binding.btnUploadVideo.isEnabled = false
////                    prepareVideoUpload(it)
//            }
//        }
        viewmodel.retriveCategory()

        getCategory()
        binding.nextBtn.setOnClickListener {

//            if (binding.txt.text?.length == 0 || categoryId.isEmpty() ){
//                Toast.makeText(this,"جميع الحقول مطلوبة",Toast.LENGTH_LONG).show()
//                binding.progressBar.hide()
//
//            }else {

            if (videoUri == null) {
                Toast.makeText(this, "يرجى رفع مقطع", Toast.LENGTH_LONG).show()

            } else {
                if(!isCompressing) {
                    binding.progressBar.show()

//                    prepareVideoUpload(videoUri!!)
                    uploadVideoToVimeoDirectlly(videoUri!!)

                } else{
                    Toast.makeText(this, "يتم ضغط المقطع", Toast.LENGTH_LONG).show()
                }
            }
//            }
        }


        binding.notNow.setOnClickListener {
            onBackPressed()
        }

    }


    private fun displayVideoThumbnail(videoUri: Uri?) {
        videoUri?.let {
            val thumbnailBitmap = ThumbnailUtils.createVideoThumbnail(
                getPathFromUri(it),
                MediaStore.Images.Thumbnails.MINI_KIND
            )

            binding.uploades.hide()

            // Assuming you have an ImageView named 'imageViewThumbnail' in your layout
            binding.imageViewThumbnail.setImageBitmap(thumbnailBitmap)

            val byteArrayOutputStream = ByteArrayOutputStream()
            thumbnailBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

            HelperUtils.setLastImageUplaode(this, base64String)


        }
    }


//    private fun getPathFromUri(uri: Uri): String {
//        // Convert Uri to file path. Implementation may vary based on your use case.
//        val cursor = contentResolver.query(uri, null, null, null, null)
//        cursor?.moveToFirst()
//        val index = cursor?.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
//        val path = cursor?.getString(index ?: 0)
//        cursor?.close()
//        return path ?: ""
//    }

    private fun getPathFromUri(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)

        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            uri.path ?: ""
        }
    }

    private fun showUploadOptions() {
        val options = arrayOf("تصوير مقطع", "اختيار من المعرض")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("يرجى الاختيار")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> requestCameraPermissionAndLaunch()
                1 -> openGalleryToSelectVideo()
            }
        }
        builder.show()
    }

    private fun requestCameraPermissionAndLaunch() {
        // Check for camera permission and launch camera
        // Similar to your existing permission check and launchCamera() call
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                launchCamera()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


    private fun openGalleryToSelectVideo() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//        startForResult.launch(intent)

        image_Video = false
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                Log.e("ayham", "permission denied")
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO),
                    REQUEST_CODE
                )
            } else {
                video()
            }

        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                Log.e("ayham", "permission denied")
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            } else {
                video()
            }
        }
    }

    private fun getCategory() {
        hideProgress()

        viewmodel.getCategory().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {

                    adapter = ActivitiesTypesAdapterForUploade(
                        1,
                        result.data.results,
                        object : OnCategroryChose {
                            override fun onCategroyChose(id: String) {
                                categoryId = id
                            }

                        })
                    val gridLayoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                    binding.activities.adapter = adapter
                    binding.activities.layoutManager = gridLayoutManager

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }


    private fun getUplaodeVideo() {
        hideProgress()

        viewmodel.getUplaodeVide().observe(this) { result -> // هون المشكلة

            Log.e("***", result.toString())
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()

                    if (result.data.msg.status != 200) {

                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG)
                            .show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        showUploadSuccessNotification()


                    } else {
                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG)
                            .show()
                        startActivity(Intent(this,HomeActivity::class.java))
                    }
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(
                        this,
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("ertyu", result.exception.toString())
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }

    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }

    private fun prepareVideoUpload(videoUri: Uri) {
        // First, create a video object on Vimeo and get the upload URL
        createVideoObject(videoUri)

        // Then, proceed with uploading the video to the received URL
    }

    private fun launchCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                takeVideoIntent.resolveActivity(packageManager)?.also {
                    startForResult.launch(takeVideoIntent)
                }
            }
        } else {
            // If permission is not granted, inform the user
            Toast.makeText(this, "يجب اعطاء اذن الكاميرا", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadVideoToVimeoDirectlly(videoUri: Uri){
        // Convert Uri to File
        var videoFile: File

        if(playableVideoPath != ""){
            videoFile = File(playableVideoPath)
        }else {
            // Convert Uri to File
            videoFile = File(getPathFromUri(videoUri))
        }

       // Log.e("****", videoUri.toString())
        Log.e("****", videoFile.toString())


        viewmodel.retriveUserUplaode(binding.txt.text.toString(), binding.txt.text.toString(), videoFile, "1") // here
        getUplaodeVideo()


//        val viemo_linkBody = videoFile.asRequestBody("file/*".toMediaType())
//        val userToken = HelperUtils.getUserToken(application.applicationContext)
//
//        val request = Request.Builder()
//            .url("http://kenzalarabnew.br-ws.com.dedivirt1294.your-server.de/api/user/uploadVideoOrImage")
//            .addHeader("Authorization",
//                "Bearer $userToken")
//            .addHeader("Content-Type", "multipart/form-data")
//            .post(viemo_linkBody)
//            .build()
//
//        val client = OkHttpClient()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                // Handle error
//                Log.e("***1", e.toString())
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (!response.isSuccessful) {
//                    // Handle error
//                    Log.e("***2", response.toString())
//                } else {
//                    // Handle successful response
//                    Log.e("***3", response.toString())
//                }
//            }
//        })
    }

    private fun uploadVideoToVimeo(videoUri: Uri, link: String, systemLink: String) {
        // Convert Uri to File
        var videoFile: File

        if(playableVideoPath != ""){
            videoFile = File(playableVideoPath)
        }else {
            // Convert Uri to File
            videoFile = File(getPathFromUri(videoUri))
        }

        // Prepare the file to be uploaded
        val requestBody = videoFile.asRequestBody("video/*".toMediaType())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file_data", videoFile.name, requestBody)
            .build()
        // Prepare the request
        val userToken = HelperUtils.getUserToken(application.applicationContext)
        val request = Request.Builder()
            .url(link) // Replace with the upload URL provided by Vimeo API
            .addHeader(
                "Authorization",
                "Bearer $userToken"
            ) // Replace with your actual access token
            .addHeader("Content-Type", "multipart/form-data")

            .post(multipartBody)
            .build()


        viewmodel.retriveUserUplaode(
            binding.txt.text.toString(),
            binding.txt.text.toString(),
            videoFile,
            "1"
        )

        getUplaodeVideo()

        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
//                    binding.btnUploadVideo.isEnabled = true
                    // Handle the error, update UI if needed
                    Toast.makeText(
                        this@UploadeVedio,
                        "Error\t" + e.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Log the response or handle it as necessary
                response.use { res ->
                    if (res.isSuccessful) {
                        // Handle the successful response
                        val responseBody = res.body?.string()
                        runOnUiThread {
                            // Here you could update UI with details from the response if needed
//                            binding.progressBar.visibility = View.GONE
//                            binding.btnUploadVideo.isEnabled = true

//                            viewmodel.retriveUserUplaode(
//                                binding.txt.text.toString(),
//                                binding.txt.text.toString(),
//                                systemLink,
//                                "1"
//                            )


                            getUplaodeVideo()

                        }


                        Log.d("UploadSSSSAAAAA", "Response: $responseBody")
                    } else {
                        // Handle the error
                        val responseBody = res.body?.string()
                        runOnUiThread {
//                            Toast.makeText(this@UploadeVedio, "Upload failed: ${res.message}", Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.GONE
//                            binding.btnUploadVideo.isEnabled = true
                        }
                        Log.e("Upload", "Error: $responseBody")
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (image_Video) {
                        requestCameraPermissionAndLaunch()
                    } else {
                        video()
                    }
                } else {
                    showRotationalDialogForPermission()
                }

                return
            }

            NotfiCode -> {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun video() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(intent, VIDEO_REQUEST_CODE)
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    // Step 2: Create a video object on Vimeo
    fun createVideoObject(vid: Uri) {
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody =
            "".toRequestBody(jsonMediaType) // You may need to provide parameters in the request body as JSON

        val request = Request.Builder()
            .url("https://api.vimeo.com/me/videos")
            .addHeader("Authorization", "Bearer cb9661c5c0ecc54dc35089b21047de84")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/vnd.vimeo.*+json;version=3.4")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // Handle the error
                Toast.makeText(
                    this@UploadeVedio,
                    "Error\t" + e.message.toString(),
                    Toast.LENGTH_LONG
                ).show()

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("Upload", "Response: ${response.body.toString()}")

                    val responseBody = response.body?.string()
                    // Extract the upload link from the response
                    val jsonObject = JSONObject(responseBody)
                    val uploadObject = jsonObject.getJSONObject("upload")
                    val uploadLink = uploadObject.getString("upload_link")


                    val link = jsonObject.getString("link")


                    uploadVideoToVimeo(vid, uploadLink, link)


                    // Proceed with the upload using this upload link
                } else {
                    // Handle the error
                }
            }
        })
    }


    // Helper method to get the file path from a Uri
    private fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            path
        } else {
            // Handle the case where the Uri is not pointing to a file that can be directly accessed by a path.
            throw IOException("Unable to get path from URI")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST_CODE -> {
//                if(data != null){
//                    val uri=data?.data
//                    imageData=get(uri)
//                    imageFile= File(imageData)
//                    binding.uploadImg.text="تم اختيار صورة"
//                    Log.d("ayhamGallery", imageData)
//                }else{
//                    showMessage(this,"لم يتم اختيار اي صورة")
//                }
            }

            VIDEO_REQUEST_CODE -> {
                if (data != null) {
                    val uri = data?.data
//                    videoUri = getPathFromUri(uri)
//                    videoUri = File(videoData)


                    videoUri = data?.data

                    uris.add(videoUri!!)
                    processVideo()

                    displayVideoThumbnail(videoUri)

//                    binding.uploadVideo.text = "تم اختيار فيديو"
//                    Log.d("ayhamVideo", videoUri.toString())
                } else {
                    showMessage(this, "لم يتم اختير اي فيديو")
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun processVideo() {

        isCompressing = true
//        Log.d("ayhamVideo2", uris.toString())
        lifecycleScope.launch {
            VideoCompressor.start(
                context = applicationContext,
                uris,
                isStreamable = false,
                sharedStorageConfiguration = SharedStorageConfiguration(
                    saveAt = SaveLocation.movies,
                    subFolderName = "my-demo-videos"
                ),
                configureWith = Configuration(
                    quality = VideoQuality.LOW,
                    videoNames = uris.map { uri -> uri.pathSegments.last() },
                    isMinBitrateCheckEnabled = true,
                ),
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {
                        //Update UI
                        if (percent <= 100)
                            runOnUiThread {

                                playableVideoPath = ""
                                uri = uris[index]
                                newSize = ""
                                progressPercent = percent

//                                 Log.d("ayhamVideo3", uris[index].toString())
                                resetProgress()

                            }
                    }

                    override fun onStart(index: Int) {

                    }

                    override fun onSuccess(index: Int, size: Long, path: String?) {

                        playableVideoPath = path
                        uri = uris[index]
                        newSize = getFileSize(size)
                        progressPercent = 100F

                        runOnUiThread {

                            resetProgress()
                        }
                        isCompressing = false
                        Log.d("ayhamVideo4", videoUri.toString())
                        videoUri = uris[index]
                    }

                    override fun onFailure(index: Int, failureMessage: String) {
                        Log.wtf("failureMessage", failureMessage)
                        isCompressing = false
                    }

                    override fun onCancelled(index: Int) {
                        Log.wtf("TAG", "compression has been cancelled")
                        isCompressing = false
                        // make UI changes, cleanup, etc
                    }
                },
            )
        }
    }

    fun resetProgress(){

        if (progressPercent > 0 && progressPercent <= 100) {

            binding.progressText.visibility = View.VISIBLE
            binding.progressText.text = "${progressPercent.toInt()} %"

            binding.progress.visibility = View.VISIBLE
            binding.progress.progress = progressPercent.toInt()
        } else {
            binding.progressText.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
        }

        if (newSize.isNotBlank()) {
            binding.newSize.text = " Size after compression : $newSize"
            binding.newSize.visibility = View.VISIBLE
        } else {
            binding.newSize.visibility = View.GONE
        }

    }

    private fun showUploadSuccessNotification() {
        val notificationId = 1
        val channelId = "upload_notification_channel"
        val channelName = "Video Upload Notifications"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)  // replace with your own icon
            .setContentTitle("Video Upload")
            .setContentText("Your video has been uploaded successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Create the NotificationChannel (required for API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Notify
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@UploadeVedio,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, notificationBuilder.build())

        }


    }


    private fun openNotificationSettings() {
        val intent = Intent().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }

                else -> {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", packageName)
                    putExtra("app_uid", applicationInfo.uid)
                }
            }
        }
        startActivity(intent)
    }

}
