package com.kunalapk.smartrecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.R
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener
import com.kunalapk.smartrecyclerview.model.LoaderModel
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder

class CustomAdapter<T>(private val activity:AppCompatActivity?,private val isPaginated:Boolean): RecyclerView.Adapter<CustomViewHolder<T>>(), Filterable {

    private var isLoading = false
    val customModelList:MutableList<Any> = arrayListOf()
    var customModelListFiltered: MutableList<Any> = customModelList


    private var onClickListener: Any? = null
    private var anyObject: Any? = null
    private var _layout: Int? = null
    private var loaderLayout: Int = R.layout.item_loader

    lateinit var smartRecyclerViewListener: SmartRecyclerViewListener<T>
    internal lateinit var viewAttachListener: ViewAttachListener<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        var layout = _layout
        if(layout==null){
            layout = smartRecyclerViewListener.getViewLayout(viewType)
        }
        if(viewType==-67){
            layout = loaderLayout
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

    fun setLoaderLayout(loaderLayout:Int){
        this.loaderLayout = loaderLayout
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
        val model = customModelListFiltered[position]
        if(model is LoaderModel){
            return -67
        }else if(_layout!=null){
            return -68
        }else{
            return smartRecyclerViewListener.getItemViewType(customModelListFiltered[position] as T)
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
        holder.bind(data = customModelListFiltered[position] as T)
    }

    fun addItems(itemList: MutableList<Any>){
        addItems(itemList,true)
    }

    fun addItems(itemList: MutableList<Any>,notifyDataSetChanged:Boolean){
        removeLoader()
        val start = customModelListFiltered.size
        customModelListFiltered.addAll(itemList)
        if(notifyDataSetChanged)
            notifyItemRangeInserted(start,itemList.size)
        isLoading = false
    }

    fun addItems(position: Int,itemList: MutableList<Any>){
        addItems(position,itemList,true)
    }

    fun addItems(position: Int,itemList: MutableList<Any>,notifyDataSetChanged:Boolean){
        removeLoader()
        customModelListFiltered.addAll(position,itemList)
        if(notifyDataSetChanged)
            notifyItemRangeInserted(position,itemList.size)
        isLoading = false
    }


    fun setLoading(isLoading:Boolean){
        this.isLoading = isLoading
    }

    fun clearItems(notifyDataSetChanged:Boolean){
        customModelListFiltered.clear()
        if(notifyDataSetChanged)
            notifyDataSetChanged()
    }

    fun clearItems(){
        clearItems(true)
    }

    fun addLoader(position: Int){
        customModelListFiltered.add(position,LoaderModel())
        notifyItemInserted(position)
    }

    fun addLoaderAtEnd(){
        val size = customModelListFiltered.size
        addLoader(size)
        notifyItemInserted(customModelListFiltered.size)
    }

    fun removeLoader(){
        (customModelListFiltered.size-1 downTo  0)
            .map { customModelListFiltered[it] }
            .filter { it is LoaderModel }
            .forEach {
                val index = customModelListFiltered.indexOf(it)
                if(index!=-1){
                    customModelListFiltered.removeAt(index)
                    notifyItemRemoved(index)
                }
            }

    }

    fun addItem(item:Any){
        customModelListFiltered.add(item)
        notifyItemInserted(customModelListFiltered.size)
    }

    fun addItem(position:Int,item:Any){
        customModelListFiltered.add(position,item)
        notifyItemInserted(position)
    }

    fun setItem(position:Int,item:Any){
        customModelListFiltered.set(position,item)
        notifyItemChanged(position)
    }

    fun removeItem(position:Int){
        customModelListFiltered.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int):Any{
        return customModelListFiltered.get(position)
    }

    fun getItems():MutableList<T>{
        return customModelListFiltered as MutableList<T>
    }

    override fun getItemCount():Int{
        if(this::smartRecyclerViewListener.isInitialized){
            smartRecyclerViewListener.setListSize(customModelListFiltered.size)
        }
        return customModelListFiltered.size
    }

    override fun getFilter(): Filter = object :Filter(){

        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val searchedText = charSequence?.trim().toString().toLowerCase()
            customModelListFiltered = when {
                searchedText.isBlank() -> {
                    customModelList
                }
                else -> {
                    val filteredList = arrayListOf<Any>()
                    customModelList
                        .filterTo(filteredList) {
                            if(it is LoaderModel){
                                false
                            }else{
                                smartRecyclerViewListener.filterSearch(searchedText,(it as T))
                            }
                        }
                    filteredList
                }
            }
            return FilterResults().apply {
                values = customModelListFiltered
            }
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            customModelListFiltered = p1?.values as MutableList<Any>
            notifyDataSetChanged()
        }
    }

    fun addItemsWithDiffUtil(newData: MutableList<Any>,callBack:DiffUtil.Callback) {
        DiffUtil.calculateDiff(callBack).dispatchUpdatesTo(this)
        customModelListFiltered.clear()
        customModelListFiltered.addAll(newData)
    }
}