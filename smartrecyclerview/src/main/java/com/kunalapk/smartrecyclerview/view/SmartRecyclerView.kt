package com.kunalapk.smartrecyclerview.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener


class SmartRecyclerView<T> : SwipeRefreshLayout {

    var recyclerView: RecyclerView? = null

    private lateinit var customAdapter:CustomAdapter<T>

    private var isPaginated:Boolean = false

    private lateinit var smartRecyclerViewListener:SmartRecyclerViewListener<T>


    private var constraintLayout:ConstraintLayout? = null
    private var shimmerView:View? = null
    private var targetPosition: Int = 0
    private var startPosition: Int = 0
    private var endPosition: Int = 0
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var mCurrentItemLocation = IntArray(2)


    constructor(context: Context):super(context){ }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        constraintLayout = ConstraintLayout(context)
        recyclerView = RecyclerView(context)
        recyclerView?.clipToPadding = false
        val newParams:ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        constraintLayout?.addView(recyclerView, newParams)
        addView(constraintLayout)
        setProgressViewOffset(false, 0, 200)
    }

    fun attachToPageSnaper(attach: Boolean){
        if(attach){
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    fun setLoaderLayout(loaderLayout: Int){
        customAdapter.setLoaderLayout(loaderLayout)
    }

    fun setCustomPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int){
        shimmerView?.setPadding(paddingLeft, paddingTop + 5, paddingRight, 0)
        recyclerView?.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    fun setBottomPadding(paddingBottom: Int){
        recyclerView?.setPadding(0, 0, 0, resources.getDimension(paddingBottom).toInt())
    }

    fun initSmartRecyclerView(activity: AppCompatActivity, smartRecyclerViewListener: SmartRecyclerViewListener<T>, isPaginated: Boolean){
        initSmartRecyclerView(activity, smartRecyclerViewListener, isPaginated, LinearLayoutManager(context))
    }

    fun initSmartRecyclerView(activity: AppCompatActivity, smartRecyclerViewListener: SmartRecyclerViewListener<T>, isPaginated: Boolean, layoutManager: RecyclerView.LayoutManager){
        this.smartRecyclerViewListener = smartRecyclerViewListener
        this.isPaginated = isPaginated
        this.setOnRefreshListener(onRefreshListener)
        attachAdapterToRecyclerView(activity, smartRecyclerViewListener, layoutManager)
    }

    fun setShimmerLayout(layout: Int){
        shimmerView = LayoutInflater.from(context).inflate(layout, this, false)
        val newParams = LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        constraintLayout?.addView(shimmerView, newParams)
    }

    fun enableDisableShimmer(showShimmer: Boolean){
        shimmerView?.visibility = if(showShimmer) View.VISIBLE else View.GONE
    }


    fun setViewAttachListener(viewAttachListener: ViewAttachListener<T>){
        this.customAdapter.viewAttachListener = viewAttachListener
    }

    fun detectCurrentVisibleItem(detectCurrentVisibleItem: Boolean){
        if(detectCurrentVisibleItem){
            if(context?.getSystemService(Context.WINDOW_SERVICE) is WindowManager){
                val point = Point()
                (context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
                videoSurfaceDefaultHeight = point.x
                screenDefaultHeight = point.y
                recyclerView?.addOnScrollListener(onScrollListener)
            }else{
                recyclerView?.removeOnScrollListener(onScrollListener)
            }
        }
    }


    fun setClickListener(clickListener: Any){
        customAdapter.setOnClickListener(clickListener)
    }

    fun setItemLayout(layout: Int){
        customAdapter.setItemLayout(layout)
    }

    fun setScrollListener(onScrollListener: RecyclerView.OnScrollListener){
        recyclerView?.addOnScrollListener(onScrollListener)
    }

    internal fun setLoading(isLoading: Boolean){
        customAdapter.setLoading(isLoading)
    }

    private val onRefreshListener: SwipeRefreshLayout.OnRefreshListener = object :
        SwipeRefreshLayout.OnRefreshListener {
        override fun onRefresh() {
            smartRecyclerViewListener.onRefresh()
        }
    }

    fun setAnyObject(anyObject: Any?){
        customAdapter.setAnyObject(anyObject)
    }

    fun setHasFixedSize(boolean: Boolean){
        recyclerView?.setHasFixedSize(boolean)
    }

    fun notifyItemChanged(position: Int){
        customAdapter.notifyItemChanged(position)
    }

    fun notifyItemRangeInserted(position: Int, itemCount: Int){
        customAdapter.notifyItemRangeInserted(position, itemCount)
    }

    fun notifyItemInserted(position: Int){
        customAdapter.notifyItemInserted(position)
    }

    fun notifyItemRemoved(position: Int){
        customAdapter.notifyItemRemoved(position)
    }

    fun notifyDataSetChanged(){
        customAdapter.notifyDataSetChanged()
    }

    fun addItems(itemList: MutableList<T>,notifyDataSetChanged:Boolean){
        discardDefaultView()
        customAdapter.addItems(itemList as MutableList<Any>,notifyDataSetChanged)
    }

    fun addItems(itemList: MutableList<T>){
        addItems(itemList,true)
    }

    fun addItems(position: Int, itemList: MutableList<T>){
        addItems(position,itemList,true)
    }

    fun addItems(position: Int, itemList: MutableList<T>,notifyDataSetChanged:Boolean){
        discardDefaultView()
        customAdapter.addItems(position, itemList as MutableList<Any>,notifyDataSetChanged)
    }

    private fun discardDefaultView(){
        enableDisableShimmer(false)
    }

    fun clearItems(){
        customAdapter.clearItems()
    }

    fun clearItems(notifyDataSetChanged:Boolean){
        customAdapter.clearItems(notifyDataSetChanged)
    }

    fun addItem(item: T){
        discardDefaultView()
        customAdapter.addItem(item as Any)
    }

    fun addItem(position: Int, item: T){
        discardDefaultView()
        customAdapter.addItem(position, item as Any)
    }

    fun setItem(position: Int, item: T){
        customAdapter.setItem(position, item as Any)
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

    private fun attachAdapterToRecyclerView(activity: AppCompatActivity, smartRecyclerViewListener: SmartRecyclerViewListener<T>, mlayoutManager: RecyclerView.LayoutManager){
        customAdapter = CustomAdapter<T>(activity = activity, isPaginated = isPaginated)
        customAdapter.smartRecyclerViewListener = smartRecyclerViewListener
        recyclerView?.apply {
            adapter = customAdapter
            layoutManager = mlayoutManager
        }
    }


    private val onScrollListener : RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.layoutManager!=null) {
                if(!recyclerView.canScrollVertically(1)) {
                    calculateCurrentVisibleItemPosition(true, recyclerView, recyclerView.layoutManager!!)
                }else{
                    calculateCurrentVisibleItemPosition(false, recyclerView, recyclerView.layoutManager!!)
                }
            }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                smartRecyclerViewListener.setCurrentItemPosition(-1)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun calculateCurrentVisibleItemPosition(isEndOfList: Boolean, recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager){
        if(layoutManager is LinearLayoutManager){
            if(!isEndOfList){
                startPosition = layoutManager.findFirstVisibleItemPosition()
                endPosition  = layoutManager.findLastVisibleItemPosition()

                if (endPosition - startPosition > 1) {
                    endPosition = startPosition + 1
                }

                // something is wrong. return.
                if (startPosition < 0 || endPosition < 0) {
                    return
                }

                // if there is more than 1 list-item on the screen
                targetPosition = if (startPosition != endPosition) {
                    if (getVisibleSurfaceHeight(startPosition, recyclerView, layoutManager) > getVisibleSurfaceHeight(
                            endPosition,
                            recyclerView,
                            layoutManager
                        ))
                        startPosition
                    else
                        endPosition
                } else {
                    startPosition
                }
            }else{
                targetPosition = getItems().size - 1
            }

            smartRecyclerViewListener.setCurrentItemPosition(targetPosition)
        }
    }

    private fun getVisibleSurfaceHeight(playPosition: Int, recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager): Int {
        (recyclerView.getChildAt((playPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())) ?: return 0).getLocationInWindow(
            mCurrentItemLocation
        )
        return if (mCurrentItemLocation[1] < 0) {
            mCurrentItemLocation[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - mCurrentItemLocation[1]
        }
    }

}