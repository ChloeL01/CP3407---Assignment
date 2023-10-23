package com.example.cp3407_assignment.ui.dogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDogReviewBinding


class DogReview : Fragment() {

    private lateinit var binding: FragmentDogReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dog_review, container, false)
        return binding.root
    }


}