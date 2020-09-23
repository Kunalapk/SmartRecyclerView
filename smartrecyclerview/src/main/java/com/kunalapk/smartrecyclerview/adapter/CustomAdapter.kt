package com.kunalapk.smartrecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.R
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener
import com.kunalapk.smartrecyclerview.model.LoaderModel

class CustomAdapter<T>(private val activity:AppCompatActivity?,private val isPaginated:Boolean): RecyclerView.Adapter<CustomViewHolder<T>>() {

    private var isLoading = false
    private val customModelList:MutableList<Any> = arrayListOf()
    private var onClickListener: Any? = null
    private var anyObject: Any? = null
    private var _layout: Int? = null

    lateinit var smartRecyclerViewListener: SmartRecyclerViewListener<T>
    internal lateinit var viewAttachListener: ViewAttachListener<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        var layout = _layout
        if(layout==null){
            layout = smartRecyclerViewListener.getViewLayout(viewType)
        }
        if(viewType==-67){
            layout = R.layout.item_loader
        }

        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater,layout, parent, false)
        return CustomViewHolder<T>(binding,anyObject,onClickListener)
    }

    override fun onViewDetachedFromWindow(holder: CustomViewHolder<T>) {
        if(this::viewAttachListener.isInitialized){
            viewAttachListener.onViewDetachedFromWindow(holder,holder.itemView,holder.adapterPosition)
        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: CustomViewHolder<T>) {
        if(this::viewAttachListener.isInitialized){
            viewAttachListener.onViewAttachedToWindow(holder,holder.itemView,holder.adapterPosition)
        }
        super.onViewAttachedToWindow(holder)
    }

    fun setOnClickListener(onClickListener:Any?){
        this.onClickListener = onClickListener
    }

    fun setItemLayout(layout:Int){
        this._layout = layout
    }

    fun setAnyObject(anyObject:Any?){
        this.anyObject = anyObject
    }

    override fun getItemViewType(position: Int): Int {
        val model = customModelList[position]
        if(model is LoaderModel){
            return -67
        }else if(_layout!=null){
            return -68
        }else{
            return smartRecyclerViewListener.getItemViewType(customModelList[position] as T)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder<T>, position: Int) {
        if (activity!=null && position >= itemCount - 3 && !isLoading) {
            val mHandler = activity.getWindow().getDecorView().getHandler()
            mHandler.post(Runnable {
                if(!isLoading && isPaginated){
                    isLoading = true
                    addLoaderAtEnd()
                    smartRecyclerViewListener.onLoadNext()
                }
            })
        }

        @Suppress("UNCHECKED_CAST")
        holder.bind(data = customModelList[position] as T)
    }

    fun addItems(itemList: MutableList<Any>){
        removeLoader()
        val start = customModelList.size
        customModelList.addAll(itemList)
        notifyItemRangeInserted(start,itemList.size)
        isLoading = false
    }

    fun setLoading(isLoading:Boolean){
        this.isLoading = isLoading
    }

    fun clearItem(){
        customModelList.clear()
        notifyDataSetChanged()
    }

    fun addLoader(position: Int){
        customModelList.add(position,LoaderModel())
        notifyItemInserted(position)
    }

    fun addLoaderAtEnd(){
        val size = customModelList.size
        addLoader(size)
        notifyItemInserted(customModelList.size)
    }

    fun removeLoader(){
        (customModelList.size-1 downTo  0)
            .map { customModelList[it] }
            .filter { it is LoaderModel }
            .forEach {
                val index = customModelList.indexOf(it)
                if(index!=-1){
                    customModelList.removeAt(index)
                    notifyItemRemoved(index)
                }
            }

    }


    fun addItem(item:Any){
        customModelList.add(item)
        notifyItemInserted(customModelList.size)
    }

    fun addItem(position:Int,item:Any){
        customModelList.add(position,item)
        notifyItemInserted(position)
    }

    fun setItem(position:Int,item:Any){
        customModelList.set(position,item)
        notifyItemChanged(position)
    }

    fun removeItem(position:Int){
        customModelList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int):Any{
        return customModelList.get(position)
    }

    fun getItems():MutableList<Any>{
        return customModelList
    }

    override fun getItemCount():Int{
        if(this::smartRecyclerViewListener.isInitialized){
            smartRecyclerViewListener.setListSize(customModelList.size)
        }
        return customModelList.size
    }
}