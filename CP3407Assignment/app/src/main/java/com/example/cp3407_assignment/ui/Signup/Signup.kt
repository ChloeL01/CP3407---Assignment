package com.example.cp3407_assignment.ui.Signup

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.cp3407_assignment.User


class Signup : Fragment() {
    private var _binding: FragmentSignupBinding? = null

    private val db = Firebase.firestore
    //private lateinit var firebaseFirestore: FirebaseFirestore

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
                val ListDogs = ""

                //val user = User(Username, Password, ListDogs, Email, PhoneNumber)
                val user = User(Username, Password, ListDogs, Email, PhoneNumber)
                db.collection("Users").document(Username).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Note saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                        Log.d("error", e.toString())
                    }

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