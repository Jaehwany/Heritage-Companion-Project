package com.ssafy.heritage.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("glide")
fun setImage(v: ImageView, url: String) {
    if (!url.isNullOrEmpty() && url != "0" && url != "null") {
        Glide.with(v.context)
            .load(url)
            .into(v)
    }
}