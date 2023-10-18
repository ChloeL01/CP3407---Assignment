package com.example.cp3407_assignment


data class User(
    val username: String? = "",
    val email: String? = "",
    val dogs: String? = "",
    val phoneNumber: String? = ""

) { // need an empty constructor for firestore to work
    constructor() : this("","", "", "")
}



