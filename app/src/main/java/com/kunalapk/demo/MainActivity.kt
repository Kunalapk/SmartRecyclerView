package com.kunalapk.demo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.helper.NotificationSharedPreferencesHelper
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
        smartRecyclerView.initSmartRecyclerView(this,smartRecyclerViewListener,true)
        smartRecyclerView.setViewAttachListener(viewAttachListener)
        smartRecyclerView.setScrollListener(recyclerViewListener)
        smartRecyclerView.setClickListener(onItemClickListener)

        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.addItem(ModelData("Hello", "test"))
        smartRecyclerView.recyclerView.addOnChildAttachStateChangeListener(this)

        NotificationSharedPreferencesHelper.storeProfileName(this,"RamShyam")
        var name = "%name% Hello"
        name = name.replace("%name%",NotificationSharedPreferencesHelper.getProfileName(this)!!)
        Toast.makeText(baseContext,name,Toast.LENGTH_LONG).show()

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
                Log.d("VisibleItem",currentPosition.toString() + " - "+ current);
            }
        }
    }

    private val viewAttachListener:ViewAttachListener<ModelData> = object :ViewAttachListener<ModelData>{

        override fun onViewAttachedToWindow(holder: CustomViewHolder<ModelData>,itemView: View,adapterPosition:Int) {
            Log.d("TAG","viewAttach - "+adapterPosition)
            itemView.rlBox?.visibility = View.VISIBLE
        }

        override fun onViewDetachedFromWindow(holder: CustomViewHolder<ModelData>,itemView: View,adapterPosition:Int) {
            Log.d("TAG","viewDetach - "+adapterPosition)
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

        override fun onRefresh() {
            //do something on refresh....
            smartRecyclerView.isRefreshing = false
            Toast.makeText(baseContext,"OnRefresh Called",Toast.LENGTH_LONG).show()
        }

        override fun onLoadNext() {
            //Toast.makeText(baseContext,"OnLoadNext",Toast.LENGTH_LONG).show()
            Handler().postDelayed(Runnable {
                val itemList = mutableListOf<ModelData>()
                itemList.add(ModelData("Hello", "test"))
                itemList.add(ModelData("Hello", "test"))
                itemList.add(ModelData("Hello", "test"))
                smartRecyclerView.addItems(itemList)
            },5000)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        Log.d("TAG","detach - "+view.findViewById<TextView>(R.id.tvNumber).text)
    }

    override fun onChildViewAttachedToWindow(view: View) {

        Log.d("TAG","attach - "+view.findViewById<TextView>(R.id.tvNumber).text)

    }

}
