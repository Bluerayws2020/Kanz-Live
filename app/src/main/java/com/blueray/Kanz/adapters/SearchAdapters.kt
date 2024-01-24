package com.blueray.Kanz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.R
import com.blueray.Kanz.api.OnProfileSearch
import com.blueray.Kanz.databinding.SearchItemBinding
import com.blueray.Kanz.model.SarchItem
import com.blueray.Kanz.model.SearchResponse
import com.blueray.Kanz.model.SearchResult
import com.squareup.picasso.Picasso

class SearchAdapters(
    var list: List<SearchResult>,
    var onProfileSearch: OnProfileSearch
)
    : RecyclerView.Adapter<SearchAdapters.MyViewHolder>() {


    inner class MyViewHolder(val binding : SearchItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.itemView.setOnClickListener {
            onProfileSearch.onProfileTargetSearch(position)
        }
        holder.binding.name.text = list[position].first_name  + " " + list[position].last_name
        holder.binding.username.text = list[position].user_name

        if (list[position].profile_image.isEmpty()){

        }else {
            Picasso.get()
                .load(list[position].profile_image)
                .placeholder(R.drawable.logo2)
                .error(R.drawable.logo2)
                .into(holder.binding.profileImage)
        }


    }



}