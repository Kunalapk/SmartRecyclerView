package com.kunalapk.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapk.smartrecyclerview.helper.SmartLogger
import com.kunalapk.smartrecyclerview.listener.OnItemClickListener
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.listener.ViewAttachListener
import com.kunalapk.smartrecyclerview.view.SmartRecyclerView
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random


class MainActivity : AppCompatActivity(), RecyclerView.OnChildAttachStateChangeListener {

    lateinit var smartRecyclerView:SmartRecyclerView<ModelData>

    private var runnable:Runnable? = null
    private var handler:Handler = Handler(Looper.getMainLooper());

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smartRecyclerView = findViewById(R.id.smartRecyclerView)
        smartRecyclerView.initSmartRecyclerView(this,smartRecyclerViewListener,true)
        smartRecyclerView.setViewAttachListener(viewAttachListener)
        smartRecyclerView.setScrollListener(recyclerViewListener)
        smartRecyclerView.setClickListener(onItemClickListener)
        //smartRecyclerView.setShimmerLayout(R.layout.layout_shimmer)
        smartRecyclerView.detectCurrentVisibleItem(true)


        addDummyItems(counter)
        btnStart.setOnClickListener {
            toggleData()
        }

        et_search.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    smartRecyclerView.setFilter(it.toString())
                }
            }
        })

        btnStop.setOnClickListener {
            if(runnable!=null){
                handler.removeCallbacks(runnable!!)
            }
        }
    }

    private var counter = 0

    private fun toggleData(){
        counter++
        if(runnable==null){
            runnable = Runnable {
                addDummyItems(counter)
                toggleData()
            }
        }
        handler.postDelayed(runnable!!,1000)
    }


    private fun addDummyItems(counter:Int){
        val list = mutableListOf<ModelData>()

        val random = (0..10).random()
        list.apply {
            add(ModelData(id = 4353, icon = "https://content.interviewbit.com/sr-logo.png" ,name = "Scaler", price =  (0..1000).random()))
            add(ModelData(id = 12121212,icon = "https://pbs.twimg.com/profile_images/899609798299205633/DZLt0_e2_400x400.jpg" ,name = "Tata Motors",price =   (0..1000).random()))
            add(ModelData(id = 22324,icon = "https://wiki.meramaal.com/wp-content/uploads/2018/03/zomato-logo.jpg" ,name =  "Zomato",price =   (0..1000).random()))
            add(ModelData(id = 43,icon = "https://seeklogo.com/images/R/reliance-logo-6CB9A8B72D-seeklogo.com.png" ,name =  "Reliance",price =   (0..1000).random()))
            add(ModelData(id = 23,icon = "https://download.logo.wine/logo/Paytm/Paytm-Logo.wine.png" ,name =  "PayTm",price =   (0..1000).random()))
            add(ModelData(id = 12,icon = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/1200px-Google_%22G%22_Logo.svg.png" ,name =  "Google",price =   (0..1000).random()))
            add(ModelData(id = 1223,icon = "https://www.logodesignlove.com/wp-content/uploads/2016/09/apple-logo-rob-janoff-01.jpg" ,name =  "Apple",price =   (0..1000).random()))
            add(ModelData(id = 1232423,icon = "https://cdn.iconscout.com/icon/free/png-256/nykaa-3384872-2822953.png" ,name =  "Nykaa",price =   (0..1000).random()))
            add(ModelData(id = 344,icon = "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/v1415386231/utypaslbyxwfuwhfdzxd.png" ,name =  "Infosys",price =   (0..1000).random()))
            add(ModelData(id = 32432,icon = "https://pbs.twimg.com/profile_images/1412430664620822530/SlhUV9_5_400x400.jpg" ,name =  "TCS",price =   (0..1000).random()))
        }

        list.sortWith(PriceComparator())
        smartRecyclerView.addItems(list)
        //smartRecyclerView.recyclerView?.scrollToPosition(0)
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
        }

        override fun onViewDetachedFromWindow(holder: CustomViewHolder<ModelData>,itemView: View,adapterPosition:Int) {

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
            return newItem.price == oldItem.price
        }

        override fun onRefresh() {
            //do something on refresh....
            smartRecyclerView.isRefreshing = false
            Toast.makeText(baseContext,"OnRefresh Called",Toast.LENGTH_LONG).show()
        }

        override fun filterSearch(searchedString: String?, model: ModelData): Boolean {
            SmartLogger.debug("TAG ","SearchedString ${model.name?.toLowerCase()?.contains(searchedString!!)} ${model.name} ${searchedString}")
            return searchedString!=null && model.name?.toLowerCase()?.contains(searchedString)==true
        }

        override fun onLoadNext() {
            Toast.makeText(this@MainActivity,"Scrolled to Bottom : LoadNextPage",Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                addDummyItems(counter)
            },2000)
        }

        override fun setCurrentItemPosition(position: Int) {
            super.setCurrentItemPosition(position)
            if(position!=-1){
                try {
                    tvMeta.text = "FirstCompletelyVisibleItem - ${smartRecyclerView.getItems()[position].name}"
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
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
