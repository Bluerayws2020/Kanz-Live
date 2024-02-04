package com.blueray.Kanz.zegoCload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.LiveVideosListItemBinding
import com.blueray.Kanz.model.ForYouLiveStraem
import com.bumptech.glide.Glide

class LiveVideosListAdapter(
    var list: List<ForYouLiveStraem> ,
    private var cLick: LiveCLick
) : RecyclerView.Adapter<LiveVideosListAdapter.LiveVideosViewHolder>() {

    inner class LiveVideosViewHolder(val binding: LiveVideosListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveVideosViewHolder {
        val binding =
            LiveVideosListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LiveVideosViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LiveVideosViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context).load(item.user_image).placeholder(R.drawable.logo)
                .into(liveVideoImage)
        }
        holder.binding.liveVideoImage.setOnClickListener {
            cLick.OnLiveClick(position)
        }
    }
}