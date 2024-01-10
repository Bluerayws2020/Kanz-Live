package com.blueray.Kanz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.api.OnProfileClick
import com.blueray.Kanz.databinding.ItemVideoBinding
import com.blueray.Kanz.model.NewAppendItItems

class VideoAdapter(var arrVideo: List<NewAppendItItems>,  var onProfileClick: OnProfileClick,context: Context) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrVideo.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
//        holder.setVideoData(arrVideo[position])
holder.binding.profiel.setOnClickListener{
    onProfileClick.onProfileClikc(position)
}
        holder.binding.description.setOnClickListener{
            onProfileClick.onProfileClikc(position)
        }


        holder.binding.shareBtn.setOnClickListener{
            onProfileClick.onProfileShare(position)
        }


        }

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root){

//        fun setVideoData(videoModel: NewAppendItItems){
//
////            binding.tvTitle.text = videoModel.videoTitle
//            binding.tvDesc.text = videoModel.userName
//            binding.videoView.setVideoPath(videoModel.videoUrl)
//            binding.videoView.setOnPreparedListener { mp ->
//                binding.progressBar.visibility = View.GONE
//
//
//                mp.start()
//                val videoRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
//                val screenRatio =
//                    binding.videoView.width.toFloat() / binding.videoView.height.toFloat()
//
////                val scale = videoRatio / screenRatio
////                if (scale > 1f) {
////                    binding.videoView.scaleX = scale
////                } else {
////                    binding.videoView.scaleY = (1f / scale)
////                }
//            }
//
//            binding.videoView.setOnCompletionListener { MediaPlayer.OnCompletionListener { mp -> mp.start() } }
//
//        }

    }
}