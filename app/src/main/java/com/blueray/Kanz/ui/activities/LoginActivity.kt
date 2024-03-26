package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.ActivityLoginBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.LoginModel
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity(), OSSubscriptionObserver {
    private val viewmodel by viewModels<AppViewModel>()

    private lateinit var binding: ActivityLoginBinding
    var playerId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startAnimation()

        HelperUtils.setDefaultLanguage(this, "ar")
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        playerId = OneSignal.getDeviceState()?.userId.toString()
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

            if (binding.userName.text?.isEmpty() == true || binding.password.text?.isEmpty() == true) {
                Toast.makeText(this, "جميع الحقول مطلوبة", Toast.LENGTH_LONG).show()

            } else {
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
        val callback = object : OnBackPressedCallback(true) {
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

    fun startAnimation() {

//        binding.scroll.show()
//        binding.splashScreen.animate()
//            .scaleY(0.25f) //scale to quarter(half x,half y)
//            .translationY(-200f)
//            .alpha(1.0f) // make it less visible
//            .setDuration(1500) // all take 1 seconds
//            .withEndAction(Runnable {
//                //animation ended
//            })
//


        val animation =
            AnimationUtils.loadAnimation(this, R.anim.scale_up).apply {
                duration = 2500
                interpolator = AccelerateDecelerateInterpolator()
            }
        lifecycleScope.launch(Dispatchers.Main) {
            delay(500)
            binding.scroll.show()
            binding.splashScreen.startAnimation(animation)
            delay(2000)
            binding.splashScreen.hide()

            binding.logo.animate()
                .alpha(1f) // make it less visible
                .setDuration(1500) // all take 1 seconds
                .withEndAction(Runnable {
                    //animation ended
                })

            //binding.splashScreen2.show()


        }

    }

    private fun getLogin() {

        viewmodel.getLogin().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 200) {
                        saveUserData(result.data.datas)
                        //             Toast.makeText(this, result.data.datas.toString(), Toast.LENGTH_LONG).show()
                        Log.d("wew", result.data.datas.toString())
                    } else {
                        Toast.makeText(this, "الرجاء التأكد من البيانات المدخلة", Toast.LENGTH_LONG)
                            .show()
                    }

                }

                is NetworkResults.Error -> {
                    Toast.makeText(this, "الرجاء التأكد من البيانات المدخلة", Toast.LENGTH_LONG)
                        .show()
                    Log.d("jeff", result.exception.toString())
                    result.exception.printStackTrace()

                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }


    fun saveUserData(model: LoginModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)


        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.id)
            putString(HelperUtils.TOKEN_KEY, model.token)
            putString("role", model.id)
            putString(HelperUtils.USERNAME, binding.userName.text?.trim().toString())
            putString(HelperUtils.PASSWORD, binding.password.text?.trim().toString())
        }.apply()

        binding.progressBar.hide()

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        //            prefManager = PrefManager(this)


    }


    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }

    override fun onOSSubscriptionChanged(p0: OSSubscriptionStateChanges?) {
        p0?.let {
            if (!it.from.isSubscribed && it.to.isSubscribed) {
                playerId = it.to.userId

            }
        }

    }


}

