package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.Kanz.databinding.ActivityLoginBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.LoginModel
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel

class LoginActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }

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
        if (HelperUtils.getUserToken(this) != "0"){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        getLogin()
        binding.signInBtn.setOnClickListener {

            if (binding.userName.text?.isEmpty() == true || binding.password.text?.isEmpty() == true){
                Toast.makeText(this,"جميع الحقول مطلوبة",Toast.LENGTH_LONG).show()

            }else {
                binding.progressBar.show()
                viewmodel.retriveLogin(
                    binding.userName.text.toString().trim(),
                    binding.password.text.toString().trim()
                )

            }
        }


//        binding.forgotPassword.setOnClickListener {
//            startActivity(Intent(this,ForgetPasswordFirstActivity::class.java))
//        }

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
                        Toast.makeText(this, result.data.status.msg.toString(), Toast.LENGTH_LONG).show()
                    }

                }

                is NetworkResults.Error -> {
                    Toast.makeText(this, result.exception.printStackTrace().toString(), Toast.LENGTH_LONG).show()
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

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        val appId = "6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        val userId = HelperUtils.getUid(this)
        val accessToken = "27ef004db2ee6dcb0b628ef56229a072122a408c"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
            }


       }

}