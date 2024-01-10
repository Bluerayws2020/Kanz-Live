package com.blueray.Kanz.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.videoliveeventsample.BaseApplication
import com.blueray.Kanz.videoliveeventsample.util.PrefManager
import com.blueray.Kanz.videoliveeventsample.util.showToast
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.EventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SplashScreen : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
//        HelperUtils.setLang(this@SplashScreen, "ar")
//        startActivity(Intent(this@SplashScreen,HomeActivity::class.java))
uid = HelperUtils.getUid(this@SplashScreen)

        Log.d("TEEEESTTTT",uid)
                lifecycleScope.launch {
                    delay(2000)

//                    user
                    if (!uid.isNullOrEmpty() && uid != "0") {

                        prefManager = PrefManager(this@SplashScreen)
                        (application as BaseApplication).initResultLiveData.observe(
                            this@SplashScreen,
                            EventObserver {
                                if (it) {
                                    autoAuthenticate { isSucceed, e ->
                                        if (e != null) showToast(e)


                                        val intent =
                                            if (isSucceed) Intent(
                                                this@SplashScreen,
                                                HomeActivity::class.java
                                            ) else

                                                Intent(
                                                this@SplashScreen,
                                                    LoginActivity::class.java
                                            )
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                    } else {
                        //make user use App

                        val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
//        lifecycleScope.launch {
//            delay(2000)
//            if (HelperUtils.getUid(this@SplashScreen) == "0") {
////            Toast.makeText(this,"you have To Login First", Toast.LENGTH_LONG).show()
//
//                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
//
//
//            }
//
//
//
//        }


    }
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = applicationContext.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        val appId = "6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        val userId = HelperUtils.getUid(this)
        val accessToken = "27ef004db2ee6dcb0b628ef56229a072122a408c"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
        }

        val params = AuthenticateParams(userId, accessToken)
        SendbirdLive.authenticate(params) { user, e ->
            if (e != null || user == null) {
                callback.invoke(false, "${e?.message}")
                return@authenticate
            }
            callback.invoke(true, null)
        }
    }

//    fun getVideosView(){
//        mainViewModel.getMainVideos().observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is NetworkResults.Success -> {
//                    binding.img.hide()
//
//                    //                    append new Items
//
//                    // Iterate over each item and add to newArrVideoModel
//                    result.data.forEach { item ->
//                        var vidLink = ""
//                        val adaptiveFile = item.vimeo_detials.files.firstOrNull { it.rendition == "adaptive" }
//                        vidLink = adaptiveFile?.link ?: item.file
//
//                        Log.d("eRTyuIOP",vidLink)
//                        newArrVideoModel.add(
//                            NewAppendItItems(
//                                item.title,
//                                item.id.toString(),
//                                item.created,
//                                vidLink,
//                                item.auther.uid,
//                                item.auther.username,
//                                item.vimeo_detials.duration
//
//                            )
//                        )
//                    }
//                    Log.d("ERTYUIO",newArrVideoModel.size.toString())
//                    videoAdapter = VideoFeedAdapter(newArrVideoModel, object : OnProfileClick {
//
//
//                        override fun onProfileClikc(pos: Int) {
//                            Log.d("TEEEESSSSTTT11", newArrVideoModel[pos].videoUrl)
//
//                        }
//
//                        override fun onProfileShare(pos: Int) {
//                            // Implement sharing functionality
//                        }
//                    },requireContext(),this)
//
//
//                    // Set up your RecyclerView layout manager
//                    binding.vidRec.layoutManager = LinearLayoutManager(
//                        context,
//                        LinearLayoutManager.VERTICAL, false
//                    )
//
//                    binding.vidRec.adapter = videoAdapter
//
//
//
//                }
//
//                is NetworkResults.Error -> {
//                    binding.img.show()
//
//                    Log.d("ERRRRor",result.exception.toString())
//                }
//                is NetworkResults.NoInternet -> TODO()
//            }
//        }
//
//    }

}