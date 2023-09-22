package com.example.cp3407_assignment.ui.Login

import android.os.Bundle
import android.view.View
import com.example.cp3407_assignment.databinding.LoginPageBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cp3407_assignment.R

class login_page : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding

    lateinit var UsernameLogin : EditText
    lateinit var PasswordLogin: EditText
    lateinit var LoginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginButton.setOnClickListener(View.OnClickListener {
            if (binding.UsernameLogin.text.toString() == "user" && binding.PasswordLogin.text.toString() == "1234"){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()

            }
        })

    }







}