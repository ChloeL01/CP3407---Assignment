package com.example.cp3407_assignment.ui.dogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDogsBinding


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDogsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dogs, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        binding.createListing.setOnClickListener {view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_dogs_to_listHireItem)
        }

        binding.CurrentlyHiringButton.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_to_currently_hiring)
        }
        binding.PreviouslyHiredButton.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_to_previously_hired)
        }    
        binding.reviewBtn.setOnClickListener {view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_dogs_to_dogReview)
        }
    }

}