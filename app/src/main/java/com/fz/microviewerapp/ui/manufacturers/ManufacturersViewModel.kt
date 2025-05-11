package com.fz.microviewerapp.ui.manufacturers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ManufacturersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the manufacturers list fragment"
    }
    var manufacturerIds = ArrayList<Long>();

    val text: LiveData<String> = _text
}