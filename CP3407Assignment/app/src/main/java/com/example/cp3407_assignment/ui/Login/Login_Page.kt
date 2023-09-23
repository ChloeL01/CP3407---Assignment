package com.example.cp3407_assignment.ui.Login

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
import com.example.cp3407_assignment.ui.Signup.Sign_up_Page

class login_page : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding

    lateinit var UsernameLogin : EditText
    lateinit var PasswordLogin: EditText
    lateinit var LoginButton: Button
    lateinit var Return_to_Sign_in_Button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginButton.setOnClickListener(View.OnClickListener {
            if (binding.UsernameLogin.text.toString() == "user" && binding.PasswordLogin.text.toString() == "1234"){
                Toast.makeText(this, "Welcome Back "+binding.UsernameLogin.text.toString(), Toast.LENGTH_SHORT).show()
                val LoginButton = findViewById<Button>(R.id.LoginButton)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
            } else {
                Toast.makeText(this, "That doesn't look right, please try again", Toast.LENGTH_SHORT).show()

            }
        })

        val Return_to_Sign_in_Button = findViewById<Button>(R.id.Return_to_Sign_in_Button)
            Return_to_Sign_in_Button.setOnClickListener {
                val intent = Intent(this, Sign_up_Page::class.java)
                startActivity(intent)
            }

    }







}