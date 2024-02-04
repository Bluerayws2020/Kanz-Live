package com.blueray.Kanz.zegoCload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blueray.Kanz.databinding.ActivityPrepareLiveBinding

class PrepareLivees : AppCompatActivity() {
    private lateinit var binding: ActivityPrepareLiveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrepareLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateLiveEventActivity::class.java))
        })
    }
}