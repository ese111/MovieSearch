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
    textView.text = text.replace("<b>", "").replace("</b>", "").replace("&lt;", "<").replace("&gt;", ">")
}

@InverseBindingAdapter(attribute = "android:text", event = "textAttrChanged")
fun getChangeText(view: TextInputEditText): String {
    return view.text.toString()
}

@BindingAdapter("textAttrChanged")
fun setTextWatcher(view: TextInputEditText, textAttrChanged: InverseBindingListener) {
    view.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            textAttrChanged.onChange()
        }
    })
}
