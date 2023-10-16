package com.example.cp3407_assignment.ui.current_hire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentHireViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Currently Hiring Fragment"
    }
    val text: LiveData<String> = _text

}