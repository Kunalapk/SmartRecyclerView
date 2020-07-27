package com.kunalapk.smartrecyclerview.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener

class SmartRecyclerView<T> : SwipeRefreshLayout{

    private lateinit var recyclerView: RecyclerView
    
    private lateinit var customAdapter:CustomAdapter<T>

    private var isPaginated:Boolean = false


    private lateinit var smartRecyclerViewListener:SmartRecyclerViewListener<T>

    constructor(context: Context):super(context){

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val constraintLayout = ConstraintLayout(context)
        recyclerView = RecyclerView(context)
        val newParams:ViewGroup.LayoutParams = ViewGroup.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        constraintLayout.addView(recyclerView,-1,newParams)
        addView(constraintLayout)
    }


    fun initSmartRecyclerView(activity: AppCompatActivity,smartRecyclerViewListener: SmartRecyclerViewListener<T>,isPaginated:Boolean){
        initSmartRecyclerView(activity,smartRecyclerViewListener,isPaginated,LinearLayoutManager(context))
    }

    fun initSmartRecyclerView(activity: AppCompatActivity,smartRecyclerViewListener: SmartRecyclerViewListener<T>,isPaginated:Boolean,layoutManager:RecyclerView.LayoutManager){
        this.smartRecyclerViewListener = smartRecyclerViewListener
        this.isPaginated = isPaginated
        this.setOnRefreshListener(onRefreshListener)
        attachAdapterToRecyclerView(activity,smartRecyclerViewListener,layoutManager)
    }


    fun setViewAttachListener(viewAttachListener: ViewAttachListener<T>){
        this.customAdapter.viewAttachListener = viewAttachListener
    }

    fun setClickListener(clickListener:Any){
        customAdapter.setOnClickListener(clickListener)
    }

    fun setScrollListener(onScrollListener:RecyclerView.OnScrollListener){
        recyclerView.addOnScrollListener(onScrollListener)
    }

    private val onRefreshListener:OnRefreshListener = object :OnRefreshListener {
        override fun onRefresh() {
            smartRecyclerViewListener.onRefresh()
        }
    }


    fun addItems(itemList: MutableList<T>){
        customAdapter.addItems(itemList as MutableList<Any>)
    }

    fun clearItem(){
        customAdapter.clearItem()
    }

    fun addItem(item:T){
        customAdapter.addItem(item as Any)
    }

    fun addItem(position:Int,item:T){
        customAdapter.addItem(position,item as Any)
    }

    fun getItems():MutableList<Any>{
        return customAdapter.getItems()
    }

    private fun attachAdapterToRecyclerView(activity: AppCompatActivity,smartRecyclerViewListener:SmartRecyclerViewListener<T>,mlayoutManager: RecyclerView.LayoutManager){
        customAdapter = CustomAdapter<T>(activity = activity,isPaginated = isPaginated)
        customAdapter.smartRecyclerViewListener = smartRecyclerViewListener
        recyclerView.apply {
            adapter = customAdapter
            layoutManager = mlayoutManager
        }
    }
}