package com.example.cp3407_assignment.ui.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangePasswordBinding


class ChangePassword : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private val passwordLength = 6
    private var newPassword = ""
    private var errors = true

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Check if all EditText fields have input
            binding.confirmNewPasswordButton.isEnabled = checkAllEditTextsNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.currentPassword.text.isEmpty() || binding.newPassword.text.isEmpty() || binding.confirmNewPassword.text.isEmpty()) {
                binding.confirmNewPasswordButton.isEnabled = false
            }
        }
    }

    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.currentPassword.toString().isNotEmpty() && binding.newPassword.toString()
            .isNotEmpty() && binding.confirmNewPassword.toString().isNotEmpty()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()


        binding.currentPassword.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.newPassword.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.confirmNewPassword.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.currentPassword.addTextChangedListener(textWatcher)
        binding.newPassword.addTextChangedListener(textWatcher)
        binding.confirmNewPassword.addTextChangedListener(textWatcher)

        binding.confirmNewPasswordButton.setOnClickListener {
            // Connect to User database and get current instance of user
            // Need to check that the password on the database is the same as what the entered
            // If not current then provide error message
            // If current update password and update Firebase
//            validateCurrentPassword()
            validateNewPassword()

            // If everything went okidoki then go back to profile page
            if (!errors) {
                view?.findNavController()
                    ?.navigate(R.id.action_changePassword_to_navigation_profile)
            }

        }
    }

    private fun updatePassword() {
        TODO("Not yet implemented")
    }

    private fun validateCurrentPassword() {
        TODO("Not yet implemented")
    }

    private fun validateNewPassword() {
        while (errors) {
            val newPasswordString = binding.newPassword.text.toString()
            val confirmPasswordString = binding.confirmNewPassword.text.toString()

            // Password rules
            val hasLetter = newPasswordString.any { it.isLetter() }
            val hasNumber = newPasswordString.any { it.isDigit() }
            val hasSpecialChar = findSpecialCharacter(newPasswordString)

            if (newPasswordString == confirmPasswordString) {
                if (newPasswordString.length >= passwordLength && hasLetter && hasNumber && hasSpecialChar) {
                    newPassword = newPasswordString
                    errors = false
                } else {
                    binding.currentPassword.text.clear()
                    binding.newPassword.text.clear()
                    binding.confirmNewPassword.text.clear()
//                    Toast.makeText(
//                        requireContext(),
//                        "Password does not satisfy requirements",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }
    }

    private fun findSpecialCharacter(input: String): Boolean {
        val specialChar = "!$@%"
        for (char in specialChar){
            if (input.contains(char)){
                return true
            }
        }
        return false
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