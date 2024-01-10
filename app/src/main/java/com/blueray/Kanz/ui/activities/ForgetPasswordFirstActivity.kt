package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import com.blueray.Kanz.databinding.ActivityForgetPasswordFirstBinding

class ForgetPasswordFirstActivity : BaseActivity() {

    private lateinit var binding : ActivityForgetPasswordFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordOtpActivity::class.java))
        }

    }
}