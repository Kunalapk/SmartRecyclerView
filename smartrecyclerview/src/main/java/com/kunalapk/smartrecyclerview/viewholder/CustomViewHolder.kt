package com.kunalapk.smartrecyclerview.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.BR

class CustomViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(data: T) {
        binding.setVariable(BR.model, data)
        //binding.setVariable(BR.clicker,onclickListener)
        //binding.setVariable(BR.position,adapterPosition)
        binding.executePendingBindings()
    }
}