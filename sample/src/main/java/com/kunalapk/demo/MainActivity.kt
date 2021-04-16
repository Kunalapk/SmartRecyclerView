package com.kunalapk.demo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.diffutils.DiffUtilCallBack
import com.kunalapk.smartrecyclerview.listener.OnItemClickListener
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener
import com.kunalapk.smartrecyclerview.view.SmartRecyclerView
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder
import kotlinx.android.synthetic.main.item_file.view.*


class MainActivity : AppCompatActivity(), RecyclerView.OnChildAttachStateChangeListener {

    lateinit var smartRecyclerView:SmartRecyclerView<ModelData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        smartRecyclerView = findViewById(R.id.smartRecyclerView)
        smartRecyclerView.initSmartRecyclerView(this,smartRecyclerViewListener,false)
        smartRecyclerView.setViewAttachListener(viewAttachListener)
        smartRecyclerView.setScrollListener(recyclerViewListener)
        smartRecyclerView.setClickListener(onItemClickListener)
        //smartRecyclerView.setShimmerLayout(R.layout.layout_shimmer)
        smartRecyclerView.detectCurrentVisibleItem(true)

        //Handler().postDelayed(Runnable {
        toggleData()
        //},2000)



    }

    private var flag = false

    private fun toggleData(){
        flag = !flag
        Handler().postDelayed(Runnable {
            addDummyItems(flag)
            toggleData()
        },2000)
    }

    private fun addDummyItems(flag:Boolean){
        val list = mutableListOf<ModelData>()

        list.apply {
            if(flag){
                add(ModelData(12121212,"Hello", "test"))
                add(ModelData(22324, "Hello", "test"))
                add(ModelData(43534, "Hello", "test"))
                add(ModelData(4323, "Hello", "test"))
            }else{
                add(ModelData(43534, "Hello", "test"))
                add(ModelData(22324, "Hello", "test"))
                add(ModelData(4323, "Hello", "test"))
                add(ModelData(12121212,"Hello", "test"))
            }

            /*add(ModelData(2399342, "Hello", "test"))
            add(ModelData(35454, "Hello", "test"))
            add(ModelData(345435, "Hello", "test"))
            add(ModelData(43534, "Hello", "test"))
            add(ModelData(4325435, "Hello", "test"))
            add(ModelData(345435, "Hello", "test"))
            add(ModelData(43543534, "Hello", "test"))
            add(ModelData(4323, "Hello", "test"))
            add(ModelData(324234, "Hello", "test"))
            add(ModelData(546456, "Hello", "test"))
            add(ModelData(234234, "Hello", "test"))
            add(ModelData(657657, "Hello", "test"))
            add(ModelData(546324, "Hello", "test"))
            add(ModelData(2787632, "Hello", "test"))
            add(ModelData(453466, "Hello", "test"))
            add(ModelData(45656745, "Hello", "test"))
            add(ModelData(456784, "Hello", "test"))
            add(ModelData(23454577, "Hello", "test"))*/
        }


        smartRecyclerView.addItemsWithDiffUtil(list)
        smartRecyclerView.recyclerView?.addOnChildAttachStateChangeListener(this)

    }

    private val onItemClickListener:OnItemClickListener<ModelData> = object : OnItemClickListener<ModelData>{
        override fun onItemClick(view:View?,model: ModelData?) {
            Toast.makeText(baseContext,model?.name,Toast.LENGTH_LONG).show()
        }
    }

    private val recyclerViewListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val currentPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val current = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                //Log.d("VisibleItem",currentPosition.toString() + " - "+ current);
            }
        }
    }

    private val viewAttachListener:ViewAttachListener<ModelData> = object :ViewAttachListener<ModelData>{

        override fun onViewAttachedToWindow(holder: CustomViewHolder<ModelData>,itemView: View,adapterPosition:Int) {
            //Log.d("TAG","viewAttach - "+adapterPosition)
            itemView.rlBox?.visibility = View.VISIBLE
        }

        override fun onViewDetachedFromWindow(holder: CustomViewHolder<ModelData>,itemView: View,adapterPosition:Int) {
            //Log.d("TAG","viewDetach - "+adapterPosition)
            itemView.rlBox?.visibility = View.GONE
        }
    }

    private val smartRecyclerViewListener:SmartRecyclerViewListener<ModelData> = object:SmartRecyclerViewListener<ModelData>{

        override fun getItemViewType(model: ModelData): Int {
            return 0
        }

        override fun getViewLayout(model: Int): Int {
            return R.layout.item_file
        }

        override fun setListSize(size: Int) {

        }

        override fun areItemsTheSame(newItem: ModelData, oldItem: ModelData): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(newItem: ModelData, oldItem: ModelData): Boolean {
            return newItem.name.equals(oldItem.name)
        }

        override fun getChangePayload(newItem: ModelData, oldItem: ModelData): Any? {
            return null
        }

        override fun onRefresh() {
            //do something on refresh....
            smartRecyclerView.isRefreshing = false
            Toast.makeText(baseContext,"OnRefresh Called",Toast.LENGTH_LONG).show()
        }

        override fun onLoadNext() {
            //Toast.makeText(baseContext,"OnLoadNext",Toast.LENGTH_LONG).show()

                /*val itemList = mutableListOf<ModelData>()
                itemList.apply {
                    add(ModelData(2787632, "Hello", "test"))
                    add(ModelData(453466, "Hello", "test"))
                    add(ModelData(45656745, "Hello", "test"))
                    add(ModelData(456784, "Hello", "test"))
                }
                smartRecyclerView.addItems(itemList)*/

        }

        override fun setCurrentItemPosition(position: Int) {
            super.setCurrentItemPosition(position)
            Log.d("TAG","CurrentItem - "+position)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        //Log.d("TAG","detach - "+view.findViewById<TextView>(R.id.tvNumber).text)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        //Log.d("TAG","attach - "+view.findViewById<TextView>(R.id.tvNumber).text)
    }

}
