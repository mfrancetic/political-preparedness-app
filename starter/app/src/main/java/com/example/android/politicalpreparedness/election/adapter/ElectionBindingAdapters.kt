package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("isVisible")
fun setEmptyTextViewVisibility(view: TextView, elections: List<Election>?) {
    if (elections != null && elections.isNotEmpty()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}