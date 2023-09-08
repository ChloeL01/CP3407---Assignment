package com.example.cp3407_assignment

//data class Dog(
//    val doggo_name: String? = "",
//    val hire_start_date: String? = "",
//    val hire_end_date: String? = "",
//    val doggo_review: String? = "",
//    val owner: String? = "",
//    var imageUrl: String
//
//) { // need an empty constructor for firestore to work
//    constructor() : this("", "", "", "", "", "")
//
//    fun getName(): String? {
//        return doggo_name
//    }
//
//    fun getImageUrl(): String {
//        return imageUrl
//    }
//
//    fun setImageUrl(url: String) {
//        imageUrl = url
//    }
//}


class Dog {
    var doggo_name: String? = null
    val hire_start_date: String? = null
    val hire_end_date: String? = null
    val doggo_review: String? = null
    val owner: String? = null
    var imageUrl: String? = null

    constructor() {
        //empty constructor needed
    }

    constructor(name: String, imageUrl: String?) {
        this.doggo_name = name
        this.imageUrl = imageUrl
    }
}