package com.example.cp3407_assignment

data class User(
    val username: String? = "",
    val email: String? = "",
    val dogsOwned: String? = "",
    val dogsHired: String? = "",
    val phoneNumber: String? = ""

) { // need an empty constructor for firestore to work
    constructor() : this("","", "", "")
}



