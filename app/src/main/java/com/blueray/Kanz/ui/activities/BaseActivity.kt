package com.blueray.Kanz.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.blueray.Kanz.helpers.ContextWrapper
import java.util.*

abstract class BaseActivity : AppCompatActivity(){

    // to save the context of the resources and save the language if destroyed
    override fun attachBaseContext(newBase: Context?) {
//        val lang = Locale.getDefault()
        val local = Locale("ar")
        val newContext = ContextWrapper.wrap(newBase!!, local)
        super.attachBaseContext(newContext)
    }

}