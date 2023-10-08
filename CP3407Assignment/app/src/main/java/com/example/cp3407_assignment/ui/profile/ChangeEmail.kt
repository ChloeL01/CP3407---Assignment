package com.example.cp3407_assignment.ui.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentChangeEmailBinding
import com.example.cp3407_assignment.databinding.FragmentChangePasswordBinding

class ChangeEmail : Fragment() {

   private lateinit var binding: FragmentChangeEmailBinding
   private var errors = true

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.confirmNewEmail.isEnabled = checkAllEditTextsNotEmpty()        // Change password button enabled when there is a value in each EditText
        }

        override fun afterTextChanged(s: Editable?) {
            // If any are empty again disable button
            if (binding.newEmail.text.isEmpty() || binding.confirmNewEmail.text.isEmpty()) {
                binding.confirmNewEmail.isEnabled = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_email, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.newEmail.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.confirmNewEmail.setOnKeyListener{view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

        binding.newEmail.addTextChangedListener(textWatcher)
        binding.confirmNewEmail.addTextChangedListener(textWatcher)

        binding.changeEmailBtn.setOnClickListener {
            validateEmail()

            if (!errors) {
                // Save new email address to database


                // Return to profile page
                view?.findNavController()?.navigate(R.id.action_changePassword_to_navigation_profile)
            }
        }
    }

    private fun validateEmail() {
        val newEmail = binding.newEmail.toString()
        val confirmNewEmail = binding.confirmNewEmail.toString()

        if (newEmail != confirmNewEmail){
            Toast.makeText(requireContext(), "Email addresses entered do not match", Toast.LENGTH_LONG).show()
            updateUI()
        } else {
            errors = false
        }
    }

    private fun updateUI() {
        binding.newEmail.removeTextChangedListener(textWatcher)
        binding.confirmNewEmail.removeTextChangedListener(textWatcher)

        binding.newEmail.text.clear()
        binding.confirmNewEmail.text.clear()

        binding.newEmail.addTextChangedListener(textWatcher)
        binding.confirmNewEmail.addTextChangedListener(textWatcher)
    }

    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.newEmail.toString().isNotEmpty() && binding.newEmail.toString().isNotEmpty()
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