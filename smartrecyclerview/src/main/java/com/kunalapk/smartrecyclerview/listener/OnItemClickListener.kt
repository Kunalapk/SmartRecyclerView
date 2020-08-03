package com.kunalapk.smartrecyclerview.listener

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(view: View?, model : T?)
}