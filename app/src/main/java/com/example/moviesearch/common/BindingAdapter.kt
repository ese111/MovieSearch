package com.example.socarassignment.common

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.socarassignment.R
import com.example.socarassignment.data.model.StationInfo

@BindingAdapter("app:startStationCircle")
fun setStartStationCircle(textView: TextView, startStation: StationInfo) {
    if (startStation.name != "") {
        textView.background =
            ContextCompat.getDrawable(textView.context, R.drawable.ic_start_marker)
    } else {
        textView.background =
            ContextCompat.getDrawable(textView.context, R.drawable.ic_station_badge_default)
    }
}

@BindingAdapter("app:arrivalStationCircle")
fun setArrivalStationCircle(textView: TextView, arrivalStation: StationInfo) {
    if (arrivalStation.name != "") {
        textView.background =
            ContextCompat.getDrawable(textView.context, R.drawable.ic_arrival_marker)
    } else {
        textView.background =
            ContextCompat.getDrawable(textView.context, R.drawable.ic_station_badge_default)
    }
}
