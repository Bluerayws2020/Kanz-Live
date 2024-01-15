package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.Kanz.adapters.ActivitiesTypesAdapter
import com.blueray.Kanz.api.OnCategroryChose
import com.blueray.Kanz.databinding.ActivitySecoundRegistrationBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel

class SecondRegistrationActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()

    companion object {
        var activtyIds = ""
    }

    private lateinit var binding: ActivitySecoundRegistrationBinding
    private lateinit var adapter: ActivitiesTypesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecoundRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, ThirdRegistrationActivity::class.java))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this, "ar")

        viewmodel.retriveCategory()
        getCategory()


        binding.signInBtn.setOnClickListener {
            if (activtyIds.isEmpty()) {
                Toast.makeText(this, "يرجى اختيار تصنيف", Toast.LENGTH_LONG).show()

            } else {
                startActivity(Intent(this, LoginActivity::class.java))

            }


        }

        binding.includeTab.back.setOnClickListener {
            onBackPressed()
        }

        binding.notNow.setOnClickListener {
            startActivity(Intent(this, ThirdRegistrationActivity::class.java))

        }
    }


    //    private fun getCategory() {
//        hideProgress()
//
//        viewmodel.getCategory().observe(this) { result ->
//
//            when (result) {
//                is NetworkResults.Success -> {
//
//
//                    adapter = ActivitiesTypesAdapter(result.data,object : OnCategroryChose {
//                        override fun onCategroyChose(id: String) {
//                            activtyIds = id
//                        }
//
//                    })
//
//                    val chipsLayoutManager = ChipsLayoutManager.newBuilder(this).build()
//                    binding.activitiesRv.adapter = adapter
//                    binding.activitiesRv.layoutManager =chipsLayoutManager
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
    private fun getCategory() {
        hideProgress()

        viewmodel.getCategory().observe(this) { result ->

            Log.e("****", result.toString())
            when (result) {
                is NetworkResults.Success -> {

                    adapter = ActivitiesTypesAdapter(0, result.data.results, object : OnCategroryChose {
                        override fun onCategroyChose(id: String) {
                            activtyIds = id
                        }
                    })

                    // Replace ChipsLayoutManager with GridLayoutManager
                    val gridLayoutManager = GridLayoutManager(this, 2)
                    binding.activitiesRv.layoutManager = gridLayoutManager
                    binding.activitiesRv.adapter = adapter

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
}