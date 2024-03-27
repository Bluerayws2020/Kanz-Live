package com.blueray.Kanz.zegoCload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.blueray.Kanz.databinding.ActivityPrepareLiveBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.blueray.Kanz.videoliveeventsample.util.showToast

class PrepareLivees : AppCompatActivity() {
    private lateinit var binding: ActivityPrepareLiveBinding
    private val viewModel: AppViewModel by viewModels()

    companion object{
        var liveId:String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrepareLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        binding.start.setOnClickListener(View.OnClickListener {
            viewModel.retrieveCreateLive()





        })
    }

    private fun getData() {
        viewModel.getCreateLive().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {


                    if (result.data.status.code == 200) {

                        if(result.data.status.message == "تم إنشاء البث المباشر بنجاح"){
                            liveId = result.data.results.room_id
                            val intent = Intent(this, CreateLiveEventActivity::class.java)
                            startActivity(intent)
                            showToast(result.data.status.message)
                            Log.w("AASCCVXBVCBD" ,result.data.status.message.toString() )
                        }else{
                            viewModel.retrieveCreateLive()
                            Log.w("AASCCVXBVCBD" ,result.data.status.message.toString() )
                        }

                        //
                    } else {
                        showToast(result.data.status.message)
                    }
                }

                is NetworkResults.Error -> {
                    showToast(result.exception.message.toString())
                }

                else -> {

                }
            }
        }
    }
}