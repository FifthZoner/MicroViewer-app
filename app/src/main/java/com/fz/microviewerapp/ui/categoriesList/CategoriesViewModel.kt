package com.fz.microviewerapp.ui.categoriesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoriesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the categories list Fragment"
    }
    val text: LiveData<String> = _text
}