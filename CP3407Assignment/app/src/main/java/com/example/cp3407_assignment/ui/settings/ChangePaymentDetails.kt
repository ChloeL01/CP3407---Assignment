package com.example.cp3407_assignment.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangePaymentDetailsBinding
import com.google.android.gms.common.util.DataUtils

class ChangePaymentDetails : Fragment() {
    private lateinit var binding: FragmentChangePaymentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_change_payment_details,
            container,
            false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }


}
