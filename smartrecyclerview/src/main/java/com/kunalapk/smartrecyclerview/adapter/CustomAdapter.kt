package com.kunalapk.smartrecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener

class CustomAdapter<T>: RecyclerView.Adapter<CustomViewHolder<T>>() {

    private val customModelList:MutableList<T> = arrayListOf()

    internal lateinit var smartRecyclerViewListener: SmartRecyclerViewListener<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layout = smartRecyclerViewListener.getViewLayout(viewType)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater,layout, parent, false)

        return CustomViewHolder<T>(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return smartRecyclerViewListener.getItemViewType(customModelList[position])
    }

    override fun onBindViewHolder(holder: CustomViewHolder<T>, position: Int) {
        holder.bind(data = customModelList[position])
    }


    internal fun addItems(itemList: MutableList<T>){
        val start = customModelList.size
        customModelList.addAll(itemList)
        notifyItemRangeInserted(start,itemList.size)
    }

    internal fun clearItem(){
        customModelList.clear()
        notifyDataSetChanged()
    }

    internal fun addItem(item:T){
        customModelList.add(item)
        notifyItemInserted(customModelList.size)
    }

    internal fun addItem(position:Int,item:T){
        customModelList.add(position,item)
        notifyItemInserted(position)
    }

    internal fun getItems():MutableList<T>{
        return customModelList
    }

    override fun getItemCount():Int{
        smartRecyclerViewListener.setListSize(customModelList.size)
        return customModelList.size
    }
}