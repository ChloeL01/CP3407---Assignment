package com.example.cp3407_assignment.ui.Signup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.User
import com.example.cp3407_assignment.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Change signup button enabled when there is a value in each EditText
            binding.confirmButton.isEnabled = checkAllEditTextsNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.userNameInput.text.toString().isEmpty() || binding.emailInput.text.toString().isEmpty() || binding.passwordInput.text.toString().isEmpty() || binding.confirmPassword.text.toString().isEmpty() || binding.phoneNumberInput.text.toString().isEmpty()) {
                binding.confirmButton.isEnabled = false
            }

        }
    }

    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.userNameInput.toString().isNotEmpty() && binding.emailInput.toString()
            .isNotEmpty() && binding.passwordInput.toString()
            .isNotEmpty() && binding.confirmPassword.toString()
            .isNotEmpty() && binding.phoneNumberText.toString().isNotEmpty()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)

        firebaseAuth = Firebase.auth

        binding.confirmButton.setOnClickListener {
//            var Password = binding.passwordInput.text.toString()
//            var Username = binding.userNameInput.text.toString()
//            var Email = binding.emailInput.text.toString()
//            val PhoneNumber = binding.phoneNumberInput.text.toString()
//            val ListDogs = ""
//
//            db.collection("Users").document(Username).get()
//                .addOnSuccessListener { documentSnapshot ->
//                    if (documentSnapshot.exists()) {
//                        Toast.makeText(
//                            context,
//                            "This username is taken, please try again",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else if (binding.phoneNumberInput.text.toString().length != 8 && binding.passwordInput.text.toString()
//                            .isNotEmpty() && binding.userNameInput.text.toString()
//                            .isNotEmpty() && binding.emailInput.text.toString().isNotEmpty()
//                    ) {
//                        Toast.makeText(
//                            context,
//                            "Please enter valid phone number",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    } else if (binding.passwordInput.text.toString()
//                            .isNotEmpty() && binding.userNameInput.text.toString()
//                            .isNotEmpty() && binding.emailInput.text.toString()
//                            .isNotEmpty() && binding.phoneNumberInput.text.toString().isNotEmpty()
//                    ) {
//
//                        val user = User(Username, Password, ListDogs, Email, PhoneNumber)
//                        db.collection("Users").document(Username).set(user)
//                            .addOnSuccessListener {
//                                Toast.makeText(
//                                    context,
//                                    "Welcome " + Username,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                findNavController().navigate(R.id.action_signup_to_navigation_home)
//                            }
//                            .addOnFailureListener { e ->
//                                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
//                                Log.d("error", e.toString())
//                            }
//
//                    }
//                }
//            val returnToLoginButton = binding.returnToLoginButton
//            returnToLoginButton.setOnClickListener {
//                findNavController().navigate(
//                    R.id.action_signup_to_login
//                )
//            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.emailInput.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.userNameInput.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.passwordInput.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.confirmPassword.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.phoneNumberInput.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

        binding.emailInput.addTextChangedListener(textWatcher)
        binding.userNameInput.addTextChangedListener(textWatcher)
        binding.passwordInput.addTextChangedListener(textWatcher)
        binding.confirmPassword.addTextChangedListener(textWatcher)
        binding.phoneNumberInput.addTextChangedListener(textWatcher)

        binding.confirmButton.setOnClickListener {
            // check if email exists
            // error message if does "email exist? do you have an account?
            // check password meet rules
            // if all good upload to authentication -> email and password
            // upload details to user collection


        }
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide keyboard
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
