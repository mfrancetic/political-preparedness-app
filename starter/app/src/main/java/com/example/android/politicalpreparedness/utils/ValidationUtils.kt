package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

const val ZIP_LENGTH = 5

fun Address.isValid(): Boolean {
    return this.line1.isNotEmpty() && this.city.isNotEmpty() && this.state.isNotEmpty() &&
            this.zip.isNotEmpty() && this.zip.length == ZIP_LENGTH
}

class ValidationTextWatcher(private val context: Context, private val editText: TextInputEditText,
                            private val layout: TextInputLayout) : TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        when (editText.id) {
            R.id.zip_edit_text -> isZipValid(context, editText, layout)
            else -> isEditTextValid(context, editText, layout)
        }
    }
}

fun areAllFieldsValid(context: Context, binding: FragmentRepresentativeBinding): Boolean {
    return isEditTextValid(context, binding.addressLine1EditText, binding.addressLine1) &&
            isEditTextValid(context, binding.cityEditText, binding.city) &&
            isZipValid(context, binding.zipEditText, binding.zip)
}

private fun isEditTextValid(context: Context, editText: TextInputEditText, layout: TextInputLayout): Boolean {
    if (editText.text.toString().trim().isEmpty()) {
        editText.error = context.getString(R.string.field_required)
        return false
    } else {
        layout.isErrorEnabled = false
    }
    return true
}

private fun isZipValid(context: Context, editText: TextInputEditText, layout: TextInputLayout): Boolean {
    if (editText.text.toString().trim().length != ZIP_LENGTH) {
        editText.error = context.getString(R.string.zip_length_must_be_5)
        return false
    } else {
        layout.isErrorEnabled = false
    }
    return true
}