package com.kunalapk.smartrecyclerview.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener

class DiffUtilCallBack<T>(var newList: MutableList<T>, var oldList: MutableList<T>,var smartRecyclerViewListener: SmartRecyclerViewListener<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return smartRecyclerViewListener.areItemsTheSame(newList[newItemPosition],oldList[oldItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return smartRecyclerViewListener.areContentsTheSame(newList[newItemPosition],oldList[oldItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return smartRecyclerViewListener.getChangePayload(newList[newItemPosition],oldList[oldItemPosition])
    }
}
