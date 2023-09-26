package com.example.cp3407_assignment.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Testing - Jess
//        binding.testButton.setOnClickListener { view: View? ->
//            view?.findNavController()?.navigate(R.id.action_navigation_profile_to_listHireItem)
//        }

    }
}