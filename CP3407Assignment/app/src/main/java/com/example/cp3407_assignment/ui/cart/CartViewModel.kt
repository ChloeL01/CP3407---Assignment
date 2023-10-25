package com.example.cp3407_assignment.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cp3407_assignment.Dog

class CartViewModel : ViewModel() {

    var doggoList = MutableLiveData<ArrayList<Dog>>()

    fun setDoggos(dogs: ArrayList<Dog>) {
        doggoList.value = dogs
    }

    fun getDoggos(): MutableLiveData<ArrayList<Dog>> {
        return doggoList
    }
}