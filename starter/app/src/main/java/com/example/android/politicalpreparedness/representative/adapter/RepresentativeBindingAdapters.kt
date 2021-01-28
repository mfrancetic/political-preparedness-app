package com.example.android.politicalpreparedness.representative.adapter

import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide
                .with(view.context)
                .load(uri)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

@BindingAdapter("stateValueAttrChanged")
fun setStateListener(spinner: Spinner, stateChange: InverseBindingListener?) {
    if (stateChange == null) {
        spinner.onItemSelectedListener = null
    } else {
        val listener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stateChange.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                stateChange.onChange()
            }
        }
        spinner.onItemSelectedListener = listener
    }
}

@InverseBindingAdapter(attribute = "stateValue")
fun Spinner.getNewValue(): String {
    val states: Array<String> = resources.getStringArray(R.array.states)
    return states[this.selectedItemPosition]
}

@BindingAdapter("isVisible")
fun setEmptyTextViewVisibility(view: TextView, representatives: List<Representative>?) {
    if (representatives != null && representatives.isNotEmpty()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}

@BindingAdapter("isProgressBarVisible")
fun setImageVisibility(view: ProgressBar, isDataLoading: Boolean) {
    if (isDataLoading) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}