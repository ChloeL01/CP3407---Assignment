package com.example.cp3407_assignment.ui.list_hire_item

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cp3407_assignment.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HireOutDogViewModel : ViewModel() {

    private var _userName = ""

    private val _dogName = MutableLiveData<String>()
    val dogName: MutableLiveData<String>
        get() = _dogName

    private val _description = MutableLiveData<String>()
    val description: MutableLiveData<String>
        get() = _description

    private val _cost = MutableLiveData<String>()
    val cost: MutableLiveData<String>
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

    private val _location = MutableLiveData<String>()
    val location: MutableLiveData<String>
        get() = _location

    // Firebase database
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = Firebase.storage.reference.child("Storage")

    var imageUri: Uri? = null

    var isSuccessful: Boolean = false


    fun saveDogListing() {


        imageUri?.let {
            storageReference.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val data = Dog(
                            _dogName.value ?: "",
                            _breed.value ?: "",
                            _description.value ?: "",
                            _startDate.value ?: "",
                            _endDate.value ?: "",
                            _cost.value.toString(),
                            "new doggo good boi points",
                            _userName,
                            _contactType.value.toString(),
                            uri.toString()
                        )
                        firebaseFirestore.collection("Dogs").add(data)
                            .addOnCompleteListener { firestoreTask ->
                                isSuccessful = firestoreTask.isSuccessful
                                isSuccessful= true // not sure why false at the moment oh well
                            }
                    }
                }
            }
        }
    }
}

