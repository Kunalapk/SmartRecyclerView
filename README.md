# SmartRecyclerView for Android
An android library to quickly setup RecyclerView(List) with SwipeRefreshLayout Support, written entirely in Kotlin.

![Works with Android](https://img.shields.io/badge/Works_with-Android-green?style=flat-square)
[![](https://jitpack.io/v/Kunalapk/SmartRecyclerView.svg)](https://jitpack.io/#Kunalapk/SmartRecyclerView)

<img src="https://media.makeameme.org/created/howd-you-do.jpg">

### Supported Platforms
-----------------------
```
Works on Android 5.0+ (API level 21+) and on Java 8+.
```

### Let's do it quickly!
---------------------------
The simplest way to start

```kotlin

allprojects {
     repositories {
	maven { url 'https://jitpack.io' }
     }
}


buildFeatures {
     dataBinding = true
}


dependencies {
     implementation 'com.github.Kunalapk:SmartRecyclerView:TAG'
}
```

### Using Activity
---------------------------

```kotlin
lateinit var smartRecyclerView:SmartRecyclerView<T>
```

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_main)

     smartRecyclerView = findViewById(R.id.smartRecyclerView)
     smartRecyclerView.apply{
     	initSmartRecyclerView(activity = this,smartRecyclerViewListener = smartRecyclerViewListener,isPaginated = true)
	setClickListener(onItemClickListener) // (optional, set clickListener on recyclerview items)
	setViewAttachListener(viewAttachListener) // (optional, set viewAttachListener on recyclerview items)
	setScrollListener(recyclerViewListener) // (optional, set setScrollListener on recyclerview items)
	setShimmerLayout(R.layout.item_loader) // (optional, set shimmer layout while user waits for the data to load)
     }
}

```

## set custom LayoutManager
```kotlin
val linearLayoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
initSmartRecyclerView(activity = this,smartRecyclerViewListener = smartRecyclerViewListener,isPaginated = true, layoutManager = linearLayoutManager)

```

## setAnyObject as extra parameter
```kotlin
val username = "Steve Jobs"
smartRecyclerView.apply{
    setAnyObject(username)
}
```

## smartRecyclerViewListener
```kotlin
private val smartRecyclerViewListener:SmartRecyclerViewListener<T> = object:SmartRecyclerViewListener<T>{

        override fun getItemViewType(model: T): Int {
            return 0 //return viewType from model
        }

        override fun getViewLayout(viewType: Int): Int {
            return R.layout.item_file // on the basis of viewType return the layout you want for the recyclerview item.
        }

        override fun setListSize(size: Int) {
	    //this method will be called whenever the smartRecyclerView undergoes any operation.
        }

        override fun onRefresh() {
            //do something on refresh....
            smartRecyclerView.isRefreshing = false
            Toast.makeText(baseContext,"OnRefresh Called",Toast.LENGTH_LONG).show()
        }

        override fun onLoadNext() {
	    // onLoadNext() will be called if isPaginated = true and user scrolls to bottom or the smartRecyclerView.
            Toast.makeText(baseContext,"OnLoadNext",Toast.LENGTH_LONG).show()
        }
    }
```

## item_file.xml example
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="model"
            type="com.kunalapk.demo.ModelData" />

        <variable
            name="position"
            type="Integer" />

        <variable
            name="clicklistener"
            type="com.kunalapk.smartrecyclerview.listener.OnItemClickListener" />
	    
    	<variable
	    name="anyclass"
	    type="String"/> <!-- use any class as an extra param -->
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="400dp"
        android:clickable="true"
        android:id="@+id/clRoot"
        android:onClick="@{() -> clicklistener.onItemClick(model)}"
	>

	<TextView
	    android:id="@+id/tvNumber"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:text="@{position + ` - ` + model.name}"
	    app:layout_constraintTop_toTopOf="parent"
	    app:layout_constraintStart_toStartOf="parent"
	    app:layout_constraintEnd_toEndOf="parent"
	    app:layout_constraintBottom_toBottomOf="parent"/>

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

## addItems to SmartRecyclerView
```kotlin
private fun addMultipleItems(list: MutableList<Model>){
    smartRecyclerView.addItems(list)
}

private fun addSingleItem(model : Model){
    smartRecyclerView.addItem(model)
}

```
