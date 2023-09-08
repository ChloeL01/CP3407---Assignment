package com.example.cp3407_assignment

data class Dog(
    val doggo_name: String? = "",
    val hire_start_date: String? = "",
    val hire_end_date: String? = "",
    val doggo_review: String? = "",
    val owner: String? = "",

    ) { // need an empty constructor for firestore to work
    constructor() : this("","", "", "", "")
}



