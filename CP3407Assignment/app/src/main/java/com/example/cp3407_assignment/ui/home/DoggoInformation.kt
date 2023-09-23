package com.example.cp3407_assignment.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDoggoInformationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        // hide nav bar
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        view.visibility = View.GONE

        Picasso.with(context)
            .load(arguments?.getString("imageUrl"))
            .fit()
            .centerCrop()
            .into(binding.imageView)
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("doggo_name")
        binding.textViewName.text = arguments?.getString("doggo_name")
        binding.textViewDescription.text = arguments?.getString("description")
        binding.textViewReviews.text = arguments?.getString("reviews")

        //show the
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    // Do custom work here
                    view.visibility = View.VISIBLE
                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}