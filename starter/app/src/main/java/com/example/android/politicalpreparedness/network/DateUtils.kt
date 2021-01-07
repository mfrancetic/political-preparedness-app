package com.example.android.politicalpreparedness.network

import java.text.SimpleDateFormat
import java.util.*

fun getToday(): String {
    val calendar = Calendar.getInstance()
    return formatDate(calendar.time)
}

private fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(date)
}