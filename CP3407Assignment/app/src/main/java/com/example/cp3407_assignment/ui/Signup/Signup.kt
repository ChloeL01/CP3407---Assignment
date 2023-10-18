package com.example.cp3407_assignment.ui.Signup

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.User
import com.example.cp3407_assignment.ValidatePassword
import com.example.cp3407_assignment.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()
    private var storageReference = Firebase.storage.reference.child("Storage")

    private var uri: Uri? = null

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
            if (binding.userNameInput.text.toString()
                    .isEmpty() || binding.emailInput.text.toString()
                    .isEmpty() || binding.passwordInput.text.toString()
                    .isEmpty() || binding.confirmPassword.text.toString()
                    .isEmpty() || binding.phoneNumberInput.text.toString().isEmpty()
            ) {
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

            val userName = binding.userNameInput.text.toString()
            val phoneNumber = binding.phoneNumberInput.text.toString()

            val email = binding.emailInput.text.toString()

            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            val validatePassword = ValidatePassword()

            if (!validatePassword.checkPasswordRules(password) && !validatePassword.checkNewPasswordsMatch(
                    password,
                    confirmPassword
                )
            ) {
                Toast.makeText(
                    requireContext(),
                    "Password does not meet requirements. Please review rules above. ",
                    Toast.LENGTH_LONG
                ).show()
                updatePasswordFields()
            } else {

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            val user = User(
                                userName, email, null, phoneNumber
                            )
                            val currentUser = firebaseAuth.currentUser?.uid
                            val userRef = db.collection("Users").document(currentUser!!)

                            val userData = mapOf(
                                "email" to user.email,
                                "phoneNumber" to user.phoneNumber,
                                "currentDogs" to user.dogs,
                                "username" to user.username
                            )

                            userRef.set(userData)
                                .addOnSuccessListener { Log.d(TAG, "User data saved successfully") }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        TAG,
                                        "Error saving user data",
                                        e
                                    )
                                }

                        } else {
                            // Handle the authentication error
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed: ${authResult.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
        }
    }

    private fun updatePasswordFields() {
        binding.passwordInput.removeTextChangedListener(textWatcher)
        binding.confirmPassword.removeTextChangedListener(textWatcher)
        binding.passwordInput.text?.clear()
        binding.confirmPassword.text?.clear()
        binding.passwordInput.addTextChangedListener(textWatcher)
        binding.confirmPassword.addTextChangedListener(textWatcher)
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
