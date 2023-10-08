package com.example.cp3407_assignment.ui.settings

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
import com.example.cp3407_assignment.ValidatePassword


class ChangePassword : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private var newPassword = ""
    private var errors = true

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.confirmNewPasswordButton.isEnabled = checkAllEditTextsNotEmpty()        // Change password button enabled when there is a value in each EditText
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.currentPassword.text.isEmpty() || binding.newPassword.text.isEmpty() || binding.confirmNewPassword.text.isEmpty()) {
                binding.confirmNewPasswordButton.isEnabled = false
            }
        }
    }

    /**
     * Checks if all EditTexts to see if they are empty
     */
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
            // Check that the password on the database is the same as what the entered
            validateCurrentPassword()
            validateNewPassword()

            // If everything went okidoki then go back to profile page
            if (!errors) {
                // Upload new password to User

                // Return to Profile page
                view?.findNavController()
                    ?.navigate(R.id.action_changePassword_to_navigation_profile)
            }
        }
    }

    private fun validateCurrentPassword() {
        // Check current password entered is the same to what is on user account.
    }

    /**
     * Validate user's new password and confirm new password input
     */
    private fun validateNewPassword() {

        val input = binding.newPassword.text.toString()
        val confirmInput = binding.confirmNewPassword.text.toString()

        val validatePassword = ValidatePassword(input, confirmInput)

        if (!validatePassword.checkPasswordRules() && !validatePassword.checkConfirmPassword()) {
            Toast.makeText(
                requireContext(),
                "Password does not meet requirements. Please review rules above. ",
                Toast.LENGTH_LONG
            ).show()

            updateUI()

        } else {

            newPassword = input
            errors = false
        }
    }

    /**
     * Updates the EditTexts
     */
    private fun updateUI() {
        binding.newPassword.removeTextChangedListener(textWatcher)
        binding.confirmNewPassword.removeTextChangedListener(textWatcher)

        binding.currentPassword.text.clear()
        binding.newPassword.text.clear()
        binding.confirmNewPassword.text.clear()

        binding.newPassword.addTextChangedListener(textWatcher)
        binding.confirmNewPassword.addTextChangedListener(textWatcher)
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