package com.example.cp3407_assignment.ui.Signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentSignupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class Signup : Fragment() {
    private var _binding: FragmentSignupBinding? = null

    lateinit var userNameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var emailInput: EditText
    lateinit var phoneNumberInput: EditText
    lateinit var confirmButton: Button
    lateinit var returnToLoginButton: Button

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // hide nav bar
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        view.visibility = View.GONE

        binding.confirmButton.setOnClickListener {
            if (binding.phoneNumberInput.text.toString().length != 8 && binding.passwordInput.text.toString()
                    .isNotEmpty() && binding.userNameInput.text.toString()
                    .isNotEmpty() && binding.emailInput.text.toString().isNotEmpty()
            ) {
                Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.passwordInput.text.toString()
                    .isNotEmpty() && binding.userNameInput.text.toString()
                    .isNotEmpty() && binding.emailInput.text.toString()
                    .isNotEmpty() && binding.phoneNumberInput.text.toString().isNotEmpty()
            ) {
                var Password = binding.passwordInput.text.toString()
                var Username = binding.userNameInput.text.toString()
                var Email = binding.emailInput.text.toString()
                val PhoneNumber = binding.phoneNumberInput.text.toString()

                Toast.makeText(context, "Welcome " + Username, Toast.LENGTH_SHORT).show()

                findNavController().navigate(
                    R.id.action_signup_to_navigation_home
                )

            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }


        }
        val returnToLoginButton = binding.returnToLoginButton
        returnToLoginButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_signup_to_login
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}