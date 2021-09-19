package com.kunalapk.smartrecyclerview.listener

interface SmartRecyclerViewListener<T> {
    fun getViewLayout(key : Int): Int
    fun getItemViewType(model : T): Int
    fun setListSize(size : Int){}
    fun setCurrentItemPosition(position : Int){ }
    fun areItemsTheSame(newItem: T, oldItem: T):Boolean{ return false}
    fun areContentsTheSame(newItem:T,oldItem:T):Boolean{ return false}
    fun getChangePayload(newItem:T,oldItem:T):Any?{ return null}
    fun filterSearch(searchedString:String?,model:T):Boolean{ return false}
    fun onRefresh() {}
    fun onLoadNext() {}
}