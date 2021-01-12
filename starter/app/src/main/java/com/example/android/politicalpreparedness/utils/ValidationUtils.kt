package com.example.android.politicalpreparedness.utils

import com.example.android.politicalpreparedness.network.models.Address

fun Address.isValid(): Boolean {
    return this.line1.isNotEmpty() && this.city.isNotEmpty() && this.state.isNotEmpty() &&
            this.zip.isNotEmpty() && this.zip.length == 5
}