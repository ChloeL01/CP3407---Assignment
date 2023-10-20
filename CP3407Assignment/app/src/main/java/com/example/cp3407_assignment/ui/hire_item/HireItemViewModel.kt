package com.example.cp3407_assignment.ui.hire_item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HireItemViewModel : ViewModel() {

    private val _startDateInfo = MutableLiveData<String>()
    val startDateInfo: MutableLiveData<String>
        get() = _startDateInfo

    private val _endDateInfo = MutableLiveData<String>()
    val endDateInfo: MutableLiveData<String>
        get() = _endDateInfo

    private val _timeInfo = MutableLiveData<String>()
    val timeInfo: MutableLiveData<String>
        get() = _timeInfo

    private val _costInfo = MutableLiveData<String>()
    val costInfo: MutableLiveData<String>
        get() = _costInfo
}