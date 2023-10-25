package com.example.cp3407_assignment.ui.previously_hired

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PreviouslyHiredViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Previously Hired Fragment"
    }
    val text: LiveData<String> = _text
}










