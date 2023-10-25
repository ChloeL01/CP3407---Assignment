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
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangeMobileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChangeMobile : Fragment() {
    private lateinit var binding: FragmentChangeMobileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var newNumber = ""

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.submitBtn.isEnabled =
                binding.mobileNumber.text.isNotEmpty()       // Change mobile number button enabled when there is a value in each EditText
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.mobileNumber.text.isEmpty()) {
                binding.submitBtn.isEnabled = false
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_mobile, container, false)

        firebaseAuth = Firebase.auth

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.mobileNumber.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.mobileNumber.addTextChangedListener(textWatcher)
        binding.submitBtn.setOnClickListener {
            // Save mobile number to database
            // Later implementation, send verification code via text as an extra verification step.

            val db = FirebaseFirestore.getInstance()

            newNumber = binding.mobileNumber.text.toString()
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            val userRef = db.collection("Users")

            userRef.document(currentUser!!).update("phoneNumber", newNumber)
                .addOnSuccessListener {
                    Log.d(TAG, "Mobile number updated successfully")
                    findNavController().navigate(R.id.action_changeMobile_to_navigation_profile)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating mobile number", e) }
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