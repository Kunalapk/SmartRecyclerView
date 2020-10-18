# SmartRecyclerView for Android
An android library to quickly setup RecyclerView(List) with SwipeRefreshLayout Support, written entirely in Kotlin.

![Works with Android](https://img.shields.io/badge/Works_with-Android-green?style=flat-square)
[![](https://jitpack.io/v/Kunalapk/SmartRecyclerView.svg)](https://jitpack.io/#Kunalapk/SmartRecyclerView)

### Getting Started

- Download & Install Android Studio - [from here](https://developer.android.com/studio/)
- Clone the project
- Import the project in Android Studio
- Go to Build > Make Project
- Connect a physical device
- Go to Run > Run 'app'

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
