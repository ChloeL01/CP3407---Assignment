package com.example.cp3407_assignment.ui.Signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.cp3407_assignment.databinding.LoginPageBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cp3407_assignment.MainActivity
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.SigninPageBinding
import com.example.cp3407_assignment.ui.Login.login_page

class Sign_up_Page : AppCompatActivity() {

    private lateinit var binding: SigninPageBinding

    lateinit var userNameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var emailInput: EditText
    lateinit var phoneNumberInput: EditText
    lateinit var confirmButton: Button
    lateinit var returnToLoginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_page)

        binding = SigninPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmButton.setOnClickListener(View.OnClickListener {
            if (binding.phoneNumberInput.text.toString().length !== 8 && binding.passwordInput.text.toString().isNotEmpty() && binding.userNameInput.text.toString().isNotEmpty() && binding.emailInput.text.toString().isNotEmpty()){
                Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
            }
            else if (binding.passwordInput.text.toString().isNotEmpty() && binding.userNameInput.text.toString().isNotEmpty() && binding.emailInput.text.toString().isNotEmpty() && binding.phoneNumberInput.text.toString().isNotEmpty()) {
                var Password = binding.passwordInput.text.toString()
                var Username = binding.userNameInput.text.toString()
                var Email = binding.emailInput.text.toString()
                val PhoneNumber = binding.phoneNumberInput.text.toString()

                Toast.makeText(this, "Welcome "+Username, Toast.LENGTH_SHORT).show()

                val confirmButton = findViewById<Button>(R.id.confirmButton)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }



        })
        val returnToLoginButton = findViewById<Button>(R.id.returnToLoginButton)
        returnToLoginButton.setOnClickListener {
            val intent = Intent(this, login_page::class.java)
            startActivity(intent)
        }
    }
}

