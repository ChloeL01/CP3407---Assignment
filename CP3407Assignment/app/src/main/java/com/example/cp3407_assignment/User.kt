package com.example.cp3407_assignment


data class User(
    val username: String? = "",
    val password: String? = "",
    val dogs: MutableList<String> = mutableListOf()

) { // need an empty constructor for firestore to work
    constructor() : this("", "", mutableListOf())
}



