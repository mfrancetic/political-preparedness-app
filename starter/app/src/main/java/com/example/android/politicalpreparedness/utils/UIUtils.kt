package com.example.android.politicalpreparedness.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun displaySnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}