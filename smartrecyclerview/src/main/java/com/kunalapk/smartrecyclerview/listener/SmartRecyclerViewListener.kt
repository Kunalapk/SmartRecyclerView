package com.kunalapk.smartrecyclerview.listener

interface SmartRecyclerViewListener<T> {
    fun getViewLayout(model : Int): Int
    fun getItemViewType(model : T): Int
    fun setListSize(size : Int)
    fun onRefresh()
}