package com.blueray.Kanz.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.databinding.VideoListItemBinding
import com.blueray.Kanz.model.NewAppendItItems
import com.blueray.Kanz.R
import com.blueray.Kanz.api.VideoClick
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

//import pl.droidsonroids.gif.GifImageView

class VideoItemAdapter(var  flag :Int,
    private var arrVideo: List<NewAppendItItems>,
    private var clikc:VideoClick,
   var context: Context,
    private var isLinearLayout: Boolean = false

) : RecyclerView.Adapter<VideoItemAdapter.MyViewHolder>() {
    fun setLinearLayoutMode(enabled: Boolean) {
        isLinearLayout = enabled
    }
    inner class MyViewHolder(val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = arrVideo.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clikc.OnVideoClic(position)

        }
        val videoItem = arrVideo[position]
//        val videoPath = videoItem.videoUrl




        holder.apply {
            Log.d("RTYU",videoItem.imageThum)

            if (videoItem.imageThum.isNullOrEmpty() || videoItem.imageThum == "https://i.vimeocdn.com/video/default"){


//                val base64String = HelperUtils.getImag(context)
//                val decodedString = Base64.decode(base64String, Base64.DEFAULT)
//                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//
//
//
//
//// Or, if you wish to use Picasso (by saving the bitmap to a file and using its URI)
//
//                Log.d("RTYU",HelperUtils.getImag(context))
//                val tempFile = File.createTempFile(HelperUtils.getImag(context), ".png")
//                val fileOutputStream = FileOutputStream(tempFile)
//                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//                fileOutputStream.flush()
//                fileOutputStream.close()

//                Picasso.get()
//                    .load(Uri.fromFile(tempFile))
//                    .placeholder(R.drawable.logo)
//                    .error(R.drawable.logo)
//                    .into(binding.gifs)


//                binding.gifs.setImageResource(R.drawable.video)
                binding.progressBar.playAnimation()
                binding.gifs.hide()

                binding.progressBar.show()

            }else {
binding.progressBar.hide()
                binding.gifs.show()

                Picasso.get()
                    .load(videoItem.imageThum)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(binding.gifs)
            }






            if (flag == 1){
                binding.statcard.hide()
                binding.statcard.hide()

                binding.txt.text = videoItem.status
                if ( videoItem.status == "published"){


                }else {
                    binding.statcard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))


                }


            }else {

            }







            Log.d("TEssssImage",videoItem.imageThum)

//            if (i)
//            Glide.with(binding.gifs.context).load(videoItem.imageThum).into(binding.gifs)

            if (videoItem.imageThum.isNullOrEmpty()){
                Log.d("ERRROR","1")
            }else {
//
//                Picasso.get()
//                    .load(videoItem.imageThum)
//                    .placeholder(R.drawable.logo)
//                    .error(R.drawable.logo)
//                    .into(binding.gifs)

            }



//            if (isLinearLayout) {
//                binding.gifs.setVideoPath(videoItem.videoUrl)
//                holder.binding.times.hide()
//                binding.gifs.setOnPreparedListener { mp ->
//
//                    mp.start()
//
//                }
//            } else {
//                binding.gifs.setVideoPath(videoItem.videoUrl)
                holder.binding.times.text  = videoItem.duration.toString()
////                Glide.with(binding.imgs.context)
//            }

        }
    }



}
