package com.example.cp3407_assignment.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.cp3407_assignment.Dog

class CreateDogBundle {

    fun createBundle(model: Dog): Bundle {
        return  bundleOf(
            "doggo_name" to model.doggo_name,
            "doggo_breed" to model.doggo_breed,
            "imageUrl" to model.imageUrl,
            "description" to model.description,
            "reviews" to model.doggo_review,
            "start_date" to model.hire_start_date,
            "end_date" to model.hire_end_date,
            "cost" to model.cost,
            "owner_id" to model.owner_id,
            "owner_contact" to model.owner_contact,
            )
    }


}