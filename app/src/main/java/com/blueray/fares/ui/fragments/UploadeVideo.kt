package com.blueray.fares.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.blueray.fares.databinding.ActivityLoginBinding
import com.blueray.fares.databinding.ActivityUploadeVedioBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class UploadeVedio : AppCompatActivity() {
    private lateinit var binding : ActivityUploadeVedioBinding


    private var videoUri: Uri? = null
    private val client = OkHttpClient()

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                launchCamera()
            } else {
                // Explain to the user that the feature is unavailable because
                // the features require a permission that the user has denied.
            }
        }

    // Handle the result of the camera activity
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                // The Intent contains the URI of the video
                videoUri = result.data?.data
                prepareVideoUpload(videoUri!!)
            }else {
            onBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadeVedioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize your views


        // Set the click listener for the record button
            // Check if we have permission to record
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) -> {
                    // Permission is granted; start the camera
                    launchCamera()
                }
                else -> {
                    // Permission is not granted; request it
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                    launchCamera()

                }
            }

//        // Set the click listener for the upload button
//        binding.btnUploadVideo.setOnClickListener {
//            videoUri?.let {
//                binding.progressBar.visibility = View.VISIBLE
//                binding.btnUploadVideo.isEnabled = false
////                    prepareVideoUpload(it)
//            }
//        }


    }
    private fun prepareVideoUpload(videoUri: Uri) {
        // First, create a video object on Vimeo and get the upload URL
        createVideoObject(videoUri)

        // Then, proceed with uploading the video to the received URL
    }
    private fun launchCamera() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startForResult.launch(takeVideoIntent)
            }
        }
    }

    private fun uploadVideoToVimeo(videoUri: Uri,link:String) {
        // Convert Uri to File
        val videoFile = File(getPath(videoUri))

        // Prepare the file to be uploaded
        val requestBody = videoFile.asRequestBody("video/*".toMediaType())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file_data", videoFile.name, requestBody)
            .build()
        // Prepare the request
        val request = Request.Builder()
            .url(link) // Replace with the upload URL provided by Vimeo API
            .addHeader("Authorization", "Bearer d5fcfcdd4e1159ac4b3b7aba38fdc1c0") // Replace with your actual access token
            .addHeader("Content-Type", "multipart/form-data")

            .post(multipartBody)
            .build()



        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.btnUploadVideo.isEnabled = true
                    // Handle the error, update UI if needed
                    Toast.makeText(this@UploadeVedio,"Error\t"+ e.message.toString(),Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Log the response or handle it as necessary
                response.use { res ->
                    if (res.isSuccessful) {
                        // Handle the successful response
                        val responseBody = res.body?.string()
                        runOnUiThread {
                            Toast.makeText(this@UploadeVedio, "Upload successful", Toast.LENGTH_LONG).show()
                            // Here you could update UI with details from the response if needed
                            binding.progressBar.visibility = View.GONE
                            binding.btnUploadVideo.isEnabled = true
                        }
                        onBackPressed()
                        Log.d("UploadSSSSAAAAA", "Response: $responseBody")
                    } else {
                        // Handle the error
                        val responseBody = res.body?.string()
                        runOnUiThread {
                            Toast.makeText(this@UploadeVedio, "Upload failed: ${res.message}", Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.GONE
                            binding.btnUploadVideo.isEnabled = true
                        }
                        Log.e("Upload", "Error: $responseBody")
                    }
                }
            }
        })
    }
    // Step 2: Create a video object on Vimeo
    fun createVideoObject(vid:Uri) {
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = "".toRequestBody(jsonMediaType) // You may need to provide parameters in the request body as JSON

        val request = Request.Builder()
            .url("https://api.vimeo.com/me/videos")
            .addHeader("Authorization", "Bearer d5fcfcdd4e1159ac4b3b7aba38fdc1c0")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/vnd.vimeo.*+json;version=3.4")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // Handle the error
                Toast.makeText(this@UploadeVedio,"Error\t"+ e.message.toString(),Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("Upload", "Response: ${response.body.toString()}")

                    val responseBody = response.body?.string()
                    // Extract the upload link from the response
                    val jsonObject = JSONObject(responseBody)
                    val uploadObject = jsonObject.getJSONObject("upload")
                    val uploadLink = uploadObject.getString("upload_link")

                    uploadVideoToVimeo(vid,uploadLink)
                    Log.d("qwertyuiolp",uploadLink)
                    Log.d("1234567ewertyu",vid.toString())

                    Log.d("Error createVideoObject", response.body.toString())

                    // Proceed with the upload using this upload link
                } else {
                    // Handle the error
                    Log.d("Error createVideoObject", response.body.toString())
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

}