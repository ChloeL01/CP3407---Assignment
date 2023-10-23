package com.example.cp3407_assignment

class Dog {
    var doggo_name: String? = null
    var doggo_breed: String? = null
    var description: String? = null
    var hire_start_date: String? = null
    var hire_end_date: String? = null
    var cost: String? = null
    var doggo_review: String? = null
    var owner_id: String? = null
    var owner_contact: String? = null
    var imageUrl: String? = null
    var hiree: String? = null

    constructor() {
        //empty constructor needed by firebase
    }

    constructor(
        name: String,
        breed: String,
        description: String,
        hire_start_date: String,
        hire_end_date: String,
        cost: String,
        review: String,
        owner_id: String,
        owner_contact: String,
        imageUrl: String,
        hiree: String
    ) {
        this.doggo_name = name
        this.doggo_breed = breed
        this.description = description
        this.hire_start_date = hire_start_date
        this.hire_end_date = hire_end_date
        this.cost = cost
        this.doggo_review = review
        this.owner_id = owner_id
        this.owner_contact = owner_contact
        this.imageUrl = imageUrl
        this.hiree = hiree
    }
}
