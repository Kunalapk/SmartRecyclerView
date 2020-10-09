package com.kunalapk.smartrecyclerview.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kunalapk.smartrecyclerview.R
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener

class SmartRecyclerView<T> : SwipeRefreshLayout {

    lateinit var recyclerView: RecyclerView
    
    private lateinit var customAdapter:CustomAdapter<T>

    private var isPaginated:Boolean = false

    private lateinit var smartRecyclerViewListener:SmartRecyclerViewListener<T>

    constructor(context: Context):super(context){

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val constraintLayout = ConstraintLayout(context)
        recyclerView = RecyclerView(context)
        recyclerView.clipToPadding = false
        val newParams:ViewGroup.LayoutParams = ViewGroup.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        constraintLayout.addView(recyclerView,-1,newParams)
        addView(constraintLayout)
    }

    fun attachToPageSnaper(attach: Boolean){
        if(attach){
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(recyclerView)
        }
    }

    fun setBottomPadding(paddingBottom:Int){
        recyclerView.setPadding(0,0,0,resources.getDimension(paddingBottom).toInt())
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

    fun setItemLayout(layout:Int){
        customAdapter.setItemLayout(layout)
    }

    fun setScrollListener(onScrollListener:RecyclerView.OnScrollListener){
        recyclerView.addOnScrollListener(onScrollListener)
    }

    internal fun setLoading(isLoading:Boolean){
        customAdapter.setLoading(isLoading)
    }

    private val onRefreshListener:OnRefreshListener = object :OnRefreshListener {
        override fun onRefresh() {
            smartRecyclerViewListener.onRefresh()
        }
    }

    fun setAnyObject(anyObject:Any?){
        customAdapter.setAnyObject(anyObject)
    }

    fun setHasFixedSize(boolean: Boolean){
        recyclerView.setHasFixedSize(boolean)
    }

    fun notifyItemChanged(position: Int){
        customAdapter.notifyItemChanged(position)
    }

    fun notifyItemRangeInserted(position: Int,itemCount:Int){
        customAdapter.notifyItemRangeInserted(position,itemCount)
    }

    fun notifyItemInserted(position: Int){
        customAdapter.notifyItemInserted(position)
    }

    fun notifyItemRemoved(position: Int){
        customAdapter.notifyItemRemoved(position)
    }

    fun addItems(itemList: MutableList<T>){
        customAdapter.addItems(itemList as MutableList<Any>)
    }

    fun addItems(position:Int,itemList:MutableList<T>){
        customAdapter.addItems(position,itemList as MutableList<Any>)
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

    fun setItem(position: Int,item: T){
        customAdapter.setItem(position,item as Any)
    }

    fun removeItem(position: Int){
        customAdapter.removeItem(position)
    }

    fun removeLoader(){
        customAdapter.removeLoader()
    }

    fun addLoader(position: Int){
        customAdapter.addLoader(position)
    }

    fun addLoaderAtEnd(){
        customAdapter.addLoaderAtEnd()
    }

    fun getItems():MutableList<T>{
        return customAdapter.getItems()
    }

    fun getItem(position: Int):Any?{
        return customAdapter.getItem(position)
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