package com.blueray.Kanz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.databinding.NotfiItemBinding
import com.blueray.Kanz.model.NofiItem


class NotficationAcdapter // this down is the basic implementation of an adapter
    (
    // todo change list model
    var list: List<NofiItem>
)
    : RecyclerView.Adapter<NotficationAcdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding : NotfiItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NotfiItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)


        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        TODO("Not yet implemented")


        holder.binding.username.text =  list[position].body
    }


}