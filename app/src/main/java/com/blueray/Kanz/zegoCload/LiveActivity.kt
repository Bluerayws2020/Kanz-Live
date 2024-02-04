package com.blueray.Kanz.zegoCload

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.FragmentLiveBinding
import com.blueray.Kanz.databinding.LiveActivityBinding

class LiveActivity : AppCompatActivity() {

    private var _binding: LiveActivityBinding? = null
    private val binding: LiveActivityBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        _binding = LiveActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, live_fragment())
            .commit()

    }
}