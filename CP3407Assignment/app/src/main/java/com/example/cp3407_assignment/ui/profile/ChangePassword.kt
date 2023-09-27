package com.example.cp3407_assignment.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangePasswordBinding


class ChangePassword : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,  R.layout.fragment_change_password, container,false)

       return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.confirmNewPasswordBtn.setOnClickListener {
            validateNewPassword()
        }
    }

    private fun validateNewPassword() {

    }


}