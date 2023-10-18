package com.example.cp3407_assignment.ui.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        firebaseAuth = Firebase.auth

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_login_to_navigation_home)
        } else {
            binding.LoginButton.setOnClickListener {
                val email = binding.UsernameLogin.text.toString()
                val password = binding.PasswordLogin.text.toString()

                binding.LoginButton.setOnClickListener {

                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUserData = firebaseAuth.currentUser
                                    val userUid = currentUserData?.uid

                                    if (userUid != null) {
                                        db.collection("users").document(userUid)
                                            .get()
                                            .addOnSuccessListener { documentSnapshot ->
                                                if (documentSnapshot.exists()) {
                                                    val username =
                                                        documentSnapshot.getString("username")
                                                    if (username != null) { // Ensure username is not null
                                                        Toast.makeText(
                                                            context,
                                                            "Welcome back $username",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        // Handle the case where the username is not found
                                                    }
                                                } else {
                                                    // Handle the case where the document doesn't exist
                                                }
                                            }
                                        findNavController().navigate(R.id.action_login_to_navigation_home)
                                    } else {
                                        // Handle the case where userUid is null
                                        Toast.makeText(
                                            context,
                                            "User UID is null",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Sign-in failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter your email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            binding.ReturnToSignInButton.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_signup)
            }
        }
    }
}
