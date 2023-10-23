package com.example.cp3407_assignment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cp3407_assignment.Dog


class HomeViewModel(private val state: SavedStateHandle) : ViewModel() {

//        private val _doggoList = MutableLiveData<ArrayList<Dog>>().apply {
//        //value = Dog()
//    }
//    var doggoList: MutableLiveData<ArrayList<Dog>> = _doggoList
    var doggoList = MutableLiveData<ArrayList<Dog>>()
//    var doggoList = state.getLiveData<ArrayList<Dog>>("liveData")

//    private val mName = MutableLiveData<String>()

//    fun saveState() {
//        state["liveData"] = doggoList.value
//    }

    fun setDoggos(dogs: ArrayList<Dog>) {
        doggoList.value = dogs
    }

    fun getDoggos(): MutableLiveData<ArrayList<Dog>> {
        return doggoList
    }


}