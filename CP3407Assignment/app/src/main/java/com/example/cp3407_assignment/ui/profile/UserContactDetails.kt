package com.example.cp3407_assignment.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentUserContactDetailsBinding


class UserContactDetails : Fragment() {

    private lateinit var binding: FragmentUserContactDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_contact_details, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        binding.emailAddress.setOnClickListener { changeEmailAddress() }
        binding.phoneNumber.setOnClickListener { changePhoneNumber() }
    }

    private fun changePhoneNumber() {
        TODO("Not yet implemented")
    }

    private fun changeEmailAddress() {
        TODO("Not yet implemented")
    }

}