package com.example.cp3407_assignment.ui.profile

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangeEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChangeEmail : Fragment() {

    private lateinit var binding: FragmentChangeEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

//    private var errors = true

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.changeEmailBtn.isEnabled =
                checkAllEditTextsNotEmpty()        // Change password button enabled when there is a value in each EditText
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.newEmail.text.isEmpty() || binding.confirmNewEmail.text.isEmpty() || binding.currentEmail.text.isEmpty()) {
                binding.changeEmailTitle.isEnabled = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_email, container, false)

        firebaseAuth = Firebase.auth

        db = FirebaseFirestore.getInstance()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.currentEmail.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.newEmail.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.confirmNewEmail.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

        binding.currentEmail.addTextChangedListener(textWatcher)
        binding.newEmail.addTextChangedListener(textWatcher)
        binding.confirmNewEmail.addTextChangedListener(textWatcher)

        binding.changeEmailBtn.setOnClickListener {
            val newEmail = binding.newEmail.text.toString()
            val confirmNewEmail = binding.confirmNewEmail.text.toString()

            if (newEmail != confirmNewEmail) {
                Toast.makeText(
                    requireContext(),
                    "Email addresses entered do not match",
                    Toast.LENGTH_LONG
                ).show()
                updateUI()
            } else {
//                errors = false
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User email updated!")

                            val user = firebaseAuth.currentUser?.uid
                            val userRef = db.collection("Users")

                            userRef.document(user!!).update("email", newEmail)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Email updated successfully")
                                    findNavController().navigate(R.id.action_changeEmail_to_navigation_profile)
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        TAG,
                                        "Error updating email",
                                        e
                                    )
                                }


                        } else {
                            // Handle the updateEmail task failure
                            Log.w(TAG, "Error updating email", task.exception)
                            // You may want to display a Toast or handle the error in some way
                        }
                    }

                }

            }

//            if (!errors) {
//                // Save new email address to database
//                // Return to profile page
//                view?.findNavController()?.navigate(R.id.action_changeEmail_to_navigation_profile)
//            }
        }
    }

    /**
     * Updates the EditTexts
     */
    private fun updateUI() {
        binding.currentEmail.removeTextChangedListener(textWatcher)
        binding.newEmail.removeTextChangedListener(textWatcher)
        binding.confirmNewEmail.removeTextChangedListener(textWatcher)

        binding.currentEmail.text.clear()
        binding.newEmail.text.clear()
        binding.confirmNewEmail.text.clear()

        binding.currentEmail.addTextChangedListener(textWatcher)
        binding.newEmail.addTextChangedListener(textWatcher)
        binding.confirmNewEmail.addTextChangedListener(textWatcher)
    }

    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.newEmail.toString().isNotEmpty() && binding.currentEmail.toString()
            .isNotEmpty() && binding.confirmNewEmail.toString().isNotEmpty()
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