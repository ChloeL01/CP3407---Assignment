package com.example.cp3407_assignment.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val TAG = "MainActivity"

    private val KEY_USER = "user"
    private val KEY_PASSWORD = "password"

    private lateinit var analytics: FirebaseAnalytics
    private val db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.button.setOnClickListener {
            saveUser()
        }
        binding.button2.setOnClickListener {
            loadUser()
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveUser() {
        val username: String = binding.editTextUser.text.toString()
        val password: String = binding.editTextPassword.text.toString()
        val user: MutableMap<String, Any> = HashMap()
        user[KEY_USER] = username
        user[KEY_PASSWORD] = password
        db.collection("Users").document("My First User").set(user)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "User saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, e.toString())
            }
    }

    private fun loadUser() {
        db.collection("Users").document("My First User").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val username = documentSnapshot.getString(KEY_USER)
                    val password = documentSnapshot.getString(KEY_PASSWORD)

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
}