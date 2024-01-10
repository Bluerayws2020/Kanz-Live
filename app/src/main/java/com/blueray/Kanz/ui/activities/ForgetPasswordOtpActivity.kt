package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import com.blueray.Kanz.databinding.ActivityForgetPasswordOtpBinding

class ForgetPasswordOtpActivity : BaseActivity() {

    private lateinit var binding : ActivityForgetPasswordOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verify.setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }

    }
}