package com.kunalapk.smartrecyclerview.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.BR

class CustomViewHolder<T>(private val binding: ViewDataBinding,private val anyObject:Any?,private val onClickListener:Any?) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(data: T) {
        binding.setVariable(BR.model, data)
        binding.setVariable(BR.position,adapterPosition)
        if(onClickListener!=null)
            binding.setVariable(BR.clicklistener,onClickListener)

        if(anyObject!=null)
            binding.setVariable(BR.anyobject,anyObject)


        binding.executePendingBindings()
    }
}