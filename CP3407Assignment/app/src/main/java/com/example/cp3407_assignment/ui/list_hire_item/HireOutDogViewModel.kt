package com.example.cp3407_assignment.ui.list_hire_item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HireOutDogViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: MutableLiveData<String>
        get() = _userName

    private val _dogName = MutableLiveData<String>()
    val dogName: MutableLiveData<String>
        get() = _dogName

    private val _description = MutableLiveData<String>()
    val description: MutableLiveData<String>
        get() = _description

    private val _cost = MutableLiveData<Double>()
    val cost: MutableLiveData<Double>
        get() = _cost

    private val _startDate = MutableLiveData<String>()
    val startDate: MutableLiveData<String>
        get() = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: MutableLiveData<String>
        get() = _endDate

    private val _breed = MutableLiveData<String>()
    val breed: MutableLiveData<String>
        get() = _breed

    private val _contactType = MutableLiveData<String>()
    val contactType: MutableLiveData<String>
        get() = _contactType


}