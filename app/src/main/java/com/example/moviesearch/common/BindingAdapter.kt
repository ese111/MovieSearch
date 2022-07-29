package com.example.moviesearch.common

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.moviesearch.R
import com.example.socarassignment.common.logger
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("app:thumbnail")
fun setThumbNail(imageView: ImageView, url: String) {
    if(url != "") {
        imageView.load(url)
    } else {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_hide_image))
    }
}

@BindingAdapter("app:textParse")
fun setTextParse(textView: TextView, text: String) {
    textView.text = text.replace("<b>", "").replace("</b>", "")
}
