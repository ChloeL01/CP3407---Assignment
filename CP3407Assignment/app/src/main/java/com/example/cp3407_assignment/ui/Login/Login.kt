package com.example.cp3407_assignment.ui.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val db = Firebase.firestore
//    private val binding get() = _binding!!

//    lateinit var UsernameLogin: EditText
//    lateinit var PasswordLogin: EditText
//    lateinit var LoginButton: Button
//    lateinit var Return_to_Sign_in_Button: Button


    private val loginLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login_page)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
//        val root: View = binding.root

//        binding.LoginButton.setOnClickListener {
//            var Username = binding.UsernameLogin.text.toString()
//            var Password = binding.PasswordLogin.text.toString()
//            if (Username.isNotEmpty() && Password.isNotEmpty()) {
//                db.collection("Users").document(Username).get()
//                    .addOnSuccessListener { documentSnapshot ->
//                        if (documentSnapshot.exists()) {
//                            val username = documentSnapshot.getString(Username)
//                            val password = documentSnapshot.getString(Password)
//                            Toast.makeText(
//                                context,
//                                "Welcome Back " + binding.UsernameLogin.text.toString(),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            findNavController().navigate(R.id.action_login_to_navigation_home)
//
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "That doesn't look right, please try again",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
//                        Log.d("Error finding User", e.toString())
//                    }
//            } else {
//                Toast.makeText(
//                    context,
//                    "Please enter a username and password",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }



        return binding.root
    }


    override fun onResume() {
        super.onResume()

        binding.LoginButton.setOnClickListener {

        }

        binding.ReturnToSignInButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_login_to_signup
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
//        binding.root
    }
}
