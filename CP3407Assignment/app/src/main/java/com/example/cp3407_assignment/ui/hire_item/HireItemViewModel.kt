package com.example.cp3407_assignment.ui.hire_item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HireItemViewModel : ViewModel() {

    private val _dateInfo = MutableLiveData<String>()
    val dateInfo: MutableLiveData<String>
        get() = _dateInfo

    private val _timeInfo = MutableLiveData<String>()
    val timeInfo: MutableLiveData<String>
        get() = _timeInfo

    private val _costInfo = MutableLiveData<String>()
    val costInfo: MutableLiveData<String>
        get() = _costInfo
}