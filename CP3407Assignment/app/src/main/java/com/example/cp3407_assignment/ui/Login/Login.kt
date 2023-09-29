package com.example.cp3407_assignment.ui.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val db = Firebase.firestore
    private val binding get() = _binding!!

    lateinit var UsernameLogin: EditText
    lateinit var PasswordLogin: EditText
    lateinit var LoginButton: Button
    lateinit var Return_to_Sign_in_Button: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login_page)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        //setContentView(binding.root)
        val root: View = binding.root


        binding.LoginButton.setOnClickListener {
            if (binding.UsernameLogin.text.toString() == "user" && binding.PasswordLogin.text.toString() == "1234") {
                Toast.makeText(
                    context,
                    "Welcome Back " + binding.UsernameLogin.text.toString(),
                    Toast.LENGTH_SHORT
                ).show()


                findNavController().navigate(R.id.action_login_to_navigation_home)

            } else {
                Toast.makeText(
                    context,
                    "That doesn't look right, please try again",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        binding.ReturnToSignInButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_login_to_signup)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


/* db.collection("Users").document(Username).set(user)
                    ..get()

db.collection("Users").document("My First User").get()
.addOnSuccessListener { documentSnapshot ->
    if (documentSnapshot.exists()) {
        val username = documentSnapshot.getString(KEY_USER)
        val password = documentSnapshot.getString(KEY_PASSWORD)

        //set a textbox to contain what was loaded
        binding.textHome.text = "Title: $username\nDescription: $password"
    } else {
        Toast.makeText(context, "Document does not exist", Toast.LENGTH_SHORT)
            .show()
    }
}
.addOnFailureListener { e ->
    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
    Log.d(TAG, e.toString())
}
}
*/