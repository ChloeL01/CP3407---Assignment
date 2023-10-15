package com.example.cp3407_assignment.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangePaymentDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        binding.submitChangesBtn.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle(
                "Placeholder: Navigate to selection website"
            )
                .setMessage("Placeholder: Later implementation of handling change of payment details for the user.\nOptions: Card, Paypal, or Google Pay")
                .setPositiveButton(
                    "Go back to Profile"
                ) { _, _ -> findNavController().navigate(R.id.action_changePaymentDetails_to_navigation_profile) }.show()
        }
    }
}
