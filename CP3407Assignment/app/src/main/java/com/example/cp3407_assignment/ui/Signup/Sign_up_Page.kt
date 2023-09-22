package com.example.cp3407_assignment.ui.Signup

import android.os.Bundle
import android.view.View
import com.example.cp3407_assignment.databinding.LoginPageBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.SigninPageBinding

class Sign_up_Page : AppCompatActivity() {

    private lateinit var binding: SigninPageBinding

    lateinit var userNameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var emailInput: EditText
    lateinit var phoneNumberInput: EditText
    lateinit var confirmButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_page)

        binding = SigninPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmButton.setOnClickListener(View.OnClickListener {
            if (binding.userNameInput.text.toString() == "user" && binding.passwordInput.text.toString() == "1234") {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

