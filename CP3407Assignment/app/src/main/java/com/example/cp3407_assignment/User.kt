package com.example.cp3407_assignment


data class User(
    val username: String? = "",
    val password: String? = "",
    val dogs: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val dogsHired: String? = ""

) { // need an empty constructor for firestore to work
    constructor() : this("", "", "","","")
}



