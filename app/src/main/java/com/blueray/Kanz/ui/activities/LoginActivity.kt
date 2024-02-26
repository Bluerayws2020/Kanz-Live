package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.blueray.Kanz.databinding.ActivityLoginBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.LoginModel
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.onesignal.OneSignal
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive

class LoginActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
        val playerId = OneSignal.getDeviceState()?.userId
//
//        // Now you can use the playerId as needed
//        Log.d("WEERDSCSACVSD", "Player ID: $playerId")

        binding.userName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.password.requestFocus()
                true
            } else {
                false
            }
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                HelperUtils.hideKeyBoard(this)
                true
            } else {
                false
            }
        }
        

        getLogin()
        binding.signInBtn.setOnClickListener {

            if (binding.userName.text?.isEmpty() == true || binding.password.text?.isEmpty() == true){
                Toast.makeText(this,"جميع الحقول مطلوبة",Toast.LENGTH_LONG).show()

            }else {
                binding.progressBar.show()
                if (playerId != null) {
                    viewmodel.retriveLogin(
                        binding.userName.text.toString().trim(),
                        binding.password.text.toString().trim(),
                        playerId
                    )
                }

            }
        }


//        binding.forgotPassword.setOnClickListener {
//            startActivity(Intent(this,ForgetPasswordFirstActivity::class.java))
//        }
        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("خروج")
                    .setMessage("هل انت متاكد من الخروج؟")
                    .setPositiveButton("نعم") { _, _ ->
                        ActivityCompat.finishAffinity(this@LoginActivity)

                    }
                    .setNegativeButton("لا", null)
                    .show()

            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun getLogin() {

        viewmodel.getLogin().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 200){
                        saveUserData(result.data.datas)
                        //             Toast.makeText(this, result.data.datas.toString(), Toast.LENGTH_LONG).show()
                        Log.d("wew" ,  result.data.datas.toString())
                    }else {
                        Toast.makeText(this, "الرجاء التأكد من البيانات المدخلة", Toast.LENGTH_LONG).show()
                    }

                }

                is NetworkResults.Error -> {
                    Toast.makeText(this, "الرجاء التأكد من البيانات المدخلة", Toast.LENGTH_LONG).show()
                    Log.d("jeff", result.exception.toString())
                    result.exception.printStackTrace()

                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }


    fun saveUserData(model: LoginModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)


        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.id)
            putString(HelperUtils.TOKEN_KEY, model.token)
            putString("role", model.id)
            putString(HelperUtils.USERNAME, binding.userName.text?.trim().toString())
            putString(HelperUtils.PASSWORD, binding.password.text?.trim().toString())
        }.apply()

        binding.progressBar.hide()

        startActivity(Intent(this,HomeActivity::class.java))
        finish()
        //            prefManager = PrefManager(this)


    }


    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }



}

