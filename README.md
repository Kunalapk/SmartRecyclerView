# SmartRecyclerView for Android
An android library to quickly setup RecyclerView(List) with SwipeRefreshLayout Support, written entirely in Kotlin.

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

Receive result

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
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
