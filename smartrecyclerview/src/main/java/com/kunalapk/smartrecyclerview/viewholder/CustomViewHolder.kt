package com.kunalapk.smartrecyclerview.viewholder

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.BR
import me.saket.bettermovementmethod.BetterLinkMovementMethod

class CustomViewHolder<T>(private val binding: ViewDataBinding,private val dataclass:Any?,private val onClickListener:Any?,private val urlClickListener:BetterLinkMovementMethod.OnLinkClickListener?) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(data: T) {
        binding.setVariable(BR.model, data)
        binding.setVariable(BR.position,adapterPosition)
        if(onClickListener!=null)
            binding.setVariable(BR.clicklistener,onClickListener)

        if(dataclass!=null)
            binding.setVariable(BR.dataclass,dataclass)

        if(urlClickListener!=null)
            binding.setVariable(BR.urlclickListener,urlClickListener)


        binding.executePendingBindings()
    }
}