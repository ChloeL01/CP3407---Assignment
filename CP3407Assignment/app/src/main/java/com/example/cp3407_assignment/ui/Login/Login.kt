package com.example.cp3407_assignment.ui.Login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val db = Firebase.firestore
    private val binding get() = _binding!!

    lateinit var UsernameLogin: EditText
    lateinit var PasswordLogin: EditText
    lateinit var LoginButton: Button
    lateinit var Return_to_Sign_in_Button: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login_page)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.LoginButton.setOnClickListener {
            var Username = binding.UsernameLogin.text.toString()
            var Password = binding.PasswordLogin.text.toString()
            if(Username.isNotEmpty() && Password.isNotEmpty()){
                db.collection("Users").document(Username).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val username = documentSnapshot.getString(Username)
                            val password = documentSnapshot.getString(Password)
                            Toast.makeText(
                                context,
                                "Welcome Back " + binding.UsernameLogin.text.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_login_to_navigation_home)

                        } else {
                            Toast.makeText(
                                context,
                                "That doesn't look right, please try again",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                        Log.d("Error finding User", e.toString())
                    }
            }
            else{
                Toast.makeText(
                    context,
                    "Please enter a username and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.ReturnToSignInButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_login_to_signup
            )
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
