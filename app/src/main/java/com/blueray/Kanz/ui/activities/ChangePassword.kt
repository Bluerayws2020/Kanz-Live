package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import com.blueray.Kanz.databinding.ActivityChangePasswordBinding

class ChangePassword : BaseActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.nextBtn.setOnClickListener { 
            startActivity(Intent(this,HomeActivity::class.java))
        }
        
    }
}