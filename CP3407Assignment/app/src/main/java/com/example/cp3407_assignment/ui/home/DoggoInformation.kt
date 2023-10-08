package com.example.cp3407_assignment.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDoggoInformationBinding
import com.squareup.picasso.Picasso


class DoggoInformation : Fragment() {
    private var _binding: FragmentDoggoInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoggoInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Picasso.with(context)
            .load(arguments?.getString("imageUrl"))
            .fit()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .into(binding.imageView)
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("doggo_name")
        binding.textViewName.text = arguments?.getString("doggo_name")
        binding.textViewBreed.text = arguments?.getString("doggo_breed")
        binding.textViewDescription.text = arguments?.getString("description")
        binding.textViewReviews.text = arguments?.getString("reviews")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}