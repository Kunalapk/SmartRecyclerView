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

class CustomAdapter<T>(private val activity:AppCompatActivity,private val isPaginated:Boolean): RecyclerView.Adapter<CustomViewHolder<T>>() {

    private var isLoading = false
    private val customModelList:MutableList<Any> = arrayListOf()
    private var onClickListener: Any? = null

    internal lateinit var smartRecyclerViewListener: SmartRecyclerViewListener<T>
    internal lateinit var viewAttachListener: ViewAttachListener<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        var layout = smartRecyclerViewListener.getViewLayout(viewType)
        if(viewType==-1){
            layout = R.layout.item_loader
        }
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater,layout, parent, false)

        return CustomViewHolder<T>(binding,onClickListener)
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

    fun setOnClickListener(onClickListener:Any){
        this.onClickListener = onClickListener
    }

    override fun getItemViewType(position: Int): Int {
        val model = customModelList[position]
        if(model is LoaderModel){
            return -1
        }else{
            return smartRecyclerViewListener.getItemViewType(customModelList[position] as T)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder<T>, position: Int) {
        if (position >= itemCount - 3 && !isLoading) {
            val mHandler = activity.getWindow().getDecorView().getHandler()
            mHandler.post(Runnable {
                if(!isLoading && isPaginated){
                    isLoading = true
                    addLoaderAtEnd()
                    smartRecyclerViewListener.onLoadNext()
                }
            })
        }
        holder.bind(data = customModelList[position] as T)
    }


    internal fun addItems(itemList: MutableList<Any>){
        removeLoaderFromEnd()
        val start = customModelList.size
        customModelList.addAll(itemList)
        notifyItemRangeInserted(start,itemList.size)
        isLoading = false
    }

    internal fun setLoading(isLoading:Boolean){
        this.isLoading = isLoading
    }

    internal fun clearItem(){
        customModelList.clear()
        notifyDataSetChanged()
    }

    internal fun addLoaderAtEnd(){
        customModelList.add(LoaderModel())
        notifyItemInserted(customModelList.size)
    }

    internal fun removeLoaderFromEnd(){
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


    internal fun addItem(item:Any){
        customModelList.add(item)
        notifyItemInserted(customModelList.size)
    }

    internal fun addItem(position:Int,item:Any){
        customModelList.add(position,item)
        notifyItemInserted(position)
    }

    internal fun setItem(position:Int,item:Any){
        customModelList.set(position,item)
        notifyItemChanged(position)
    }

    internal fun removeItem(position:Int){
        customModelList.removeAt(position)
        notifyItemRemoved(position)
    }

    internal fun getItem(position: Int):Any{
        return customModelList.get(position)
    }

    internal fun getItems():MutableList<Any>{
        return customModelList
    }

    override fun getItemCount():Int{
        smartRecyclerViewListener.setListSize(customModelList.size)
        return customModelList.size
    }
}