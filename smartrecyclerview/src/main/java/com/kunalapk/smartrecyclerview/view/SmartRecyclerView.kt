package com.kunalapk.smartrecyclerview.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener

class SmartRecyclerView<T> : SwipeRefreshLayout{

    private lateinit var recyclerView: RecyclerView
    
    private lateinit var customAdapter:CustomAdapter<T>

    private lateinit var smartRecyclerViewListener:SmartRecyclerViewListener<T>

    constructor(context: Context):super(context){

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val constraintLayout = ConstraintLayout(context)
        recyclerView = RecyclerView(context)
        constraintLayout.addView(recyclerView)
        addView(constraintLayout)
    }


    fun initSmartRecyclerView(context: Context,smartRecyclerViewListener: SmartRecyclerViewListener<T>){
        this.smartRecyclerViewListener = smartRecyclerViewListener
        this.setOnRefreshListener(onRefreshListener)
        //addRecyclerView()
        attachAdapterToRecyclerView(context,smartRecyclerViewListener)
    }


    private val onRefreshListener:OnRefreshListener = object :OnRefreshListener {
        override fun onRefresh() {
            smartRecyclerViewListener.onRefresh()
        }
    }

    private fun addRecyclerView(){
        recyclerView = RecyclerView(context)
        addView(recyclerView)
        //recyclerView.setBackgroundColor(R.)
    }

    fun addItems(itemList: MutableList<T>){
        customAdapter.addItems(itemList)
    }

    fun clearItem(){
        customAdapter.clearItem()
    }

    fun addItem(item:T){
        customAdapter.addItem(item)
    }

    fun addItem(position:Int,item:T){
        customAdapter.addItem(position,item)
    }

    fun getItems():MutableList<T>{
        return customAdapter.getItems()
    }

    private fun attachAdapterToRecyclerView(context: Context,smartRecyclerViewListener:SmartRecyclerViewListener<T>){
        customAdapter = CustomAdapter<T>()
        customAdapter.smartRecyclerViewListener = smartRecyclerViewListener
        recyclerView.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}