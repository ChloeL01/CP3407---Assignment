package com.example.cp3407_assignment.ui.list_hire_item

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cp3407_assignment.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HireOutDogViewModel : ViewModel() {

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

    // Later implementation
//    private val _location = MutableLiveData<String>()

    private val auth = FirebaseAuth.getInstance()
    private val _user: FirebaseUser? = auth.currentUser

    // Firebase database
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = Firebase.storage.reference.child("Storage")

    private val _userName =
        firebaseFirestore.collection("Users").document(_user.toString()).get().toString()

    var imageUri: Uri? = null

    var isSuccessful: Boolean = false
//
//    fun updateDogName(name: String) {
//        _dogName.value = name
//    }
//
//    fun updateDescription(description: String) {
//        _description.value = description
//    }
//
//    fun updatePrice(price: String) {
//        _cost.value = price
//    }

    /*
    Saves to database
     */
    fun saveDogListing() {
        storageReference = storageReference.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageReference.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val upload = Dog(
                            _dogName.value ?: "",
                            _breed.value ?: "",
                            _description.value ?: "",
                            _startDate.value ?: "",
                            _endDate.value ?: "",
                            _cost.value.toString(),
                            "",
                            _userName,
                            _contactType.value.toString(),
                            uri.toString()
                        )
                        firebaseFirestore.collection("Dogs").add(upload)
                            .addOnCompleteListener { firestoreTask ->
                                isSuccessful = firestoreTask.isSuccessful
                            }
                    }
                }
            }
        }
    }
}