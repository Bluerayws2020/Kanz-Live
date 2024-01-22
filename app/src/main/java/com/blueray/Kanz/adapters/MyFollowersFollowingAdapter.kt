package com.blueray.Kanz.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.R
import com.blueray.Kanz.api.FollowerClick
import com.blueray.Kanz.databinding.FollowersItemBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.model.FollowingList
import com.bumptech.glide.Glide

class MyFollowersFollowingAdapter(
    // todo change list model
    var context: Context,
    var list: List<FollowingList>,
    var followClikc: FollowerClick
) : RecyclerView.Adapter<MyFollowersFollowingAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: FollowersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FollowersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(list[position].picture).placeholder(R.drawable.logo)
            .into(holder.binding.profileImage)

        holder.binding.name.text = list[position].user_name
        holder.binding.username.hide()

        holder.binding.username.setBackgroundColor(Color.alpha(R.color.lightGreen))


        if (list[position].is_following == "true") {
            holder.binding.follow.text = "الغاء المتابعة"
            holder.binding.follow.setBackgroundResource(R.drawable.un_follow)

        } else {
            holder.binding.follow.text = "متابعة"
            holder.binding.follow.setBackgroundResource(R.drawable.btnfollow)
//
        }

       //todo check why the text doesn't change correctly and there is a problem with the api response
         holder.binding.follow.setOnClickListener {
             followClikc.onFollowClikcs(position)
//            if (list[position].is_following == "true") {
//
//                list[position].is_following = "false"
////                list[position].flag = 0
//
//            } else {
//
//                list[position].is_following = "true"
//
//            }


        }

    }


}