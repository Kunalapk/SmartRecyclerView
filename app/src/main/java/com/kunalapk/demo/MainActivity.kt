package com.kunalapk.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.view.SmartRecyclerView


class MainActivity : AppCompatActivity() {

    lateinit var smartRecyclerView:SmartRecyclerView<ModelData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        smartRecyclerView = findViewById(R.id.smartRecyclerView)
        smartRecyclerView.initSmartRecyclerView(this,smartRecyclerViewListener,true)

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
            Toast.makeText(baseContext,"OnLoadNext",Toast.LENGTH_LONG).show()

        }
    }

}
