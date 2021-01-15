package com.example.android.politicalpreparedness.utils

import java.util.*

fun getToday(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}