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
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangePasswordBinding


class ChangePassword : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private var newPassword: String = ""

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed in this case
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Check if all EditText fields have input
            binding.confirmNewPasswordBtn.isEnabled = checkAllEditTextsNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            // Not needed in this case
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
        // Inflate the layout for this fragment

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

        binding.confirmNewPasswordBtn.setOnClickListener {
            // TODO connect to User database and get current instance of user
            // TODO need to check that the password on the database is the same as what the entered
            // If not current then provide error message
            // If current update password and update Firebase
           validatePassword()
        }
    }

    private fun validatePassword() {
        // TODO need to check if user has followed password rules
        if (binding.newPassword.toString() == binding.confirmNewPassword.toString()){
            newPassword = binding.newPassword.toString()
        } else {
            Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()
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