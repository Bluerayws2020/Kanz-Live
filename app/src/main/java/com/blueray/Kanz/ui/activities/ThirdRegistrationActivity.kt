package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.Kanz.adapters.ActivitiesTypesAdapter
import com.blueray.Kanz.databinding.ActivityThirdRegistrationBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.RegisterModel
import com.blueray.Kanz.ui.viewModels.AppViewModel


class ThirdRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityThirdRegistrationBinding
    private val viewmodel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.signInBtn.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
        }

binding.includeTap.back.setOnClickListener {
    onBackPressed()
}

//binding.countryCode.setText("+962")


        binding.countryCode.setDefaultCountryUsingNameCode("+962")
        binding.chekBoxss.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Code to execute when the checkbox is checked

            } else {
                // Code to execute when the checkbox is unchecked
            }
        }

//        val editTexts = listOf(binding.bandName, binding.userNameEt,binding.passwordTxt,binding.ConfirmPassword,binding.phoneNumber,binding.email)

//        editTexts.forEachIndexed { index, editText ->
//            editText.setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_NEXT -> {
//                        // Move to the next EditText if it's not the last one
//                        if (index < editTexts.size - 1) {
//                            editTexts[index + 1].requestFocus()
//                        }
//                        true
//                    }
//                    EditorInfo.IME_ACTION_DONE -> {
//                        // Hide the keyboard if it's the last EditText
//                        if (index == editTexts.size - 1) {
//                            HelperUtils.hideKeyBoard(this)
//                        }
//                        true
//                    }
//                    else -> false
//                }
//            }
//        }


        if (RegistrationActivity.signupType == "1") {
//            binding.banlabel.hide()
//            binding.bandName.hide()

        } else {
//            binding.banlabel.hide()
//            binding.bandName.hide()
        }

        getCreateAccounts()

        binding.nextBtn.setOnClickListener {
            if (binding.phoneNumber.text?.isEmpty() == false) {
//                if (binding.userNameEt.text?.isEmpty() == false) {
//
//
//                    if (binding.ConfirmPassword.text.toString() == binding.passwordTxt.text.toString()) {
//showProgress()

//                        if (RegistrationActivity.signupType == "1") {

                if (binding.chekBoxss.isChecked == false) {
                    showToast("يجب الموافقة على الشروط والاحكام")

                }else {

                    showProgress()
                    val data = RegisterModel(
                        RegistrationActivity.firstName.toString(),
                        RegistrationActivity.lastName.toString(),
                       country_phone_id =  binding.countryCode.getSelectedCountryCode().toInt(),
                        //todo check the birthdate format error
                        date_of_birth = RegistrationActivity.barithDate,
                        sex = RegistrationActivity.genderId.toInt(),
                        phone =  binding.phoneNumber.text.toString(),
                        email = binding.email.text.toString(),
                        user_name = RegistrationActivity.userName.toString(),
                        hashtags_ids =ActivitiesTypesAdapter.selected_items
                        ,
                        password = RegistrationActivity.passwordTxt.toString(),

                    )
                    viewmodel.retriveCreateAccount(

                        data
//                        RegistrationActivity.firstName.toString(),
//                        RegistrationActivity.lastName.toString(),
//                        RegistrationActivity.genderId,
//                        RegistrationActivity.natonalId,
//                        RegistrationActivity.residantPlace,
//                        ActivitiesTypesAdapter.selected_items.joinToString(","),
//                        RegistrationActivity.userName.toString(),
//                        binding.email.text.toString(),
//                        binding.phoneNumber.text.toString(),
//                        RegistrationActivity.passwordTxt.toString(),
//                        RegistrationActivity.barithDate
                    )

                }

//                        }else {
//                            viewmodel.retriveBandName(
//
//                   RegistrationActivity.bandName,
//                    RegistrationActivity.natonalId,
//                    RegistrationActivity.residantPlace,
//                    ActivitiesTypesAdapter.selected_items.joinToString(","),
//                    binding.userNameEt.text.toString(),
//                    binding.email.text.toString(),
//                    binding.phoneNumber.text.toString(),
//                    binding.passwordTxt.text.toString(),
//                    RegistrationActivity.squadNumber

//                      }      )
            }
//                    } else {
//                        binding.ConfirmPassword.setError("كلمة السر غير مطابقة")
//                    }


//                } else {
//                    binding.userNameEt.setError("حقل ضروري ")
        }
//            } else {
//                binding.userNameEt.setError("حقل ضروري ")

//            }
//        }


    }


    private fun getCreateAccounts() {
        hideProgress()

        viewmodel.getCreateAccount().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.msgs == 200) {

                        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

                        sharedPreferences.edit().apply {
                            putString(HelperUtils.UID_KEY, result.data.results.id)
                            putString(HelperUtils.TOKEN_KEY , result.data.results.token)

                            putString(HelperUtils.USERNAME, RegistrationActivity.userName)
                            putString(HelperUtils.PASSWORD, RegistrationActivity.passwordTxt)



                        }.apply()


                        Log.d("wertyuiop",HelperUtils.getUserToken(this))
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, result.data.msg.msg.toString(), Toast.LENGTH_LONG)
                            .show()
                    }


                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
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
    fun saveUserData(model: RegisterModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
Log.d("TESTTTTLOOG",model.toString())

        sharedPreferences.edit().apply {

            putString(HelperUtils.USERNAME, RegistrationActivity.userName)
            putString(HelperUtils.PASSWORD, RegistrationActivity.passwordTxt)


        }.apply()
        startActivity(Intent(this,HomeActivity::class.java))
                    finish()

//        //            prefManager = PrefManager(this)
//        (application as BaseApplication).initResultLiveData.observe(this, EventObserver {
//            if (it) {
//                autoAuthenticate { isSucceed, e ->
//                    if (e != null) showToast(e)
//                    binding.progressBar.hide()
//                    startActivity(Intent(this,HomeActivity::class.java))
//                    finish()
//                }
//            } else {
//                showToast("يوجد خلل 4003")
//            }
//        })

    }

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        binding.progressBar.show()
        val appId ="6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        val userId = "65"
        val accessToken = "27ef004db2ee6dcb0b628ef56229a072122a408c"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
        }


    }


}

//    private fun getCreateBandAccounts() {
//        hideProgress()

//        viewmodel.getBandAccount().observe(this) { result ->
//            hideProgress()
//
//            when (result) {
//                is NetworkResults.Success -> {
//                    if (result.data.status.status == 200){
//                        saveUserData(result.data)
//
//                    }else {
//                        Toast.makeText(this,result.data.status.msg.toString(),Toast.LENGTH_LONG).show()
//                    }
//
//
//                }
//
//                is NetworkResults.Error -> {
//                    result.exception.printStackTrace()
//                    hideProgress()
//                }
//
//                else -> hideProgress()
//            }
//        }
//    }

//    fun saveUserData(model:UserLoginModel){
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//
//        sharedPreferences.edit().apply {
//            putString(HelperUtils.UID_KEY, model.data.uid)
//            putString("role", model.data.uid)
//
//            putString(HelperUtils.USERNAME, binding.userNameEt.text?.trim().toString())
//            putString(HelperUtils.PASSWORD, binding.passwordTxt.text?.trim().toString())
//
//
//
//        }.apply()
//        startActivity(Intent(this,HomeActivity::class.java))
//
//    }




//}