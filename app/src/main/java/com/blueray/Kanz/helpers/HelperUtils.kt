package com.blueray.Kanz.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale


object HelperUtils {

    const val ROLE_KEY = "Role_Key"
    const val NAME_KEY = "Name_key"
    const val MAIL_KEY: String = "Mail_key"
    const val BASE_URL = "https://siyam.br-ws.com/"
    const val SHARED_PREF = "Fares"
     const val UID_KEY = "UID"
    const val TOKEN_KEY = "TOKEN"


     const val USERNAME = "USERNAME"
     const val PASSWORD = "PASSWORD"

    var LANG = "en"

    fun String.toStringRequestBody(): RequestBody {
        return this.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun setDefaultLanguage(context: Context, lang: String?) {
        val locale = Locale("ar")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }
    fun getLang(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lang", "ar")!!
    }


    fun hideKeyBoard(activity: Activity) {
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        (activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else false
    }


    @SuppressLint("HardwareIds")
    fun getAndroidID(mContext: Context?): String =
        Settings.Secure.getString(mContext?.contentResolver, Settings.Secure.ANDROID_ID)

    fun showMessage(context : Context, message : String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
    fun getUid(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        return sharedPreferences.getString(UID_KEY,"0")!!
    }


    fun getUserName(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        return sharedPreferences.getString(USERNAME,"1")!!
    }
    fun getPassword(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        return sharedPreferences.getString(PASSWORD,"1")!!
    }
    fun setLang(mContext: Context?,lang: String?){
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("lang",lang)
        editor.apply {  }
    }




    fun setLastImageUplaode(mContext: Context?,img: String?){
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("img",img)
        editor.apply {  }
    }
    fun getImag(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        return sharedPreferences.getString("img","0")!!
    }

    fun getUserToken(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY,"0")!!
        }



}

