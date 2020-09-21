package com.kunalapk.smartrecyclerview.viewholder

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.BR

class CustomViewHolder<T>(private val binding: ViewDataBinding,private val dataclass:Any?,private val viewmodel: ViewModel?,private val onClickListener:Any?) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(data: T) {
        binding.setVariable(BR.model, data)
        binding.setVariable(BR.position,adapterPosition)
        if(onClickListener!=null)
            binding.setVariable(BR.clicklistener,onClickListener)

        if(dataclass!=null)
            binding.setVariable(BR.dataclass,dataclass)

        if(viewmodel!=null)
            binding.setVariable(BR.viewmodel,viewmodel)

        binding.executePendingBindings()
    }
}