package com.example.cp3407_assignment.ui.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentProfileBinding

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        firebaseAuth = Firebase.auth

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("Users").document(currentUser!!)

        userRef.addSnapshotListener{docSnapshot, firebaseFireStoreException->
            if (firebaseFireStoreException != null){
                Log.w(TAG, "Listen failed.", firebaseFireStoreException)
            }

            if (docSnapshot != null && docSnapshot.exists()){
                binding.userName.text = docSnapshot["username"].toString()
                Log.d(TAG, "Current user username: ${docSnapshot["username"]}")
            } else {
                Log.d(TAG, "Current user username: null")
            }
        }

        binding.changePasswordBtn.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_profile_to_changePassword)
        }

        binding.emailAddress.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_profile_to_changeEmail)
        }

        binding.phoneNumber.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(R.id.action_navigation_profile_to_changeMobile)
        }


        binding.logoutBtn.setOnClickListener {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    findNavController().navigate(R.id.action_navigation_profile_to_login)
                }
        }

        binding.changePaymentDetailsBtn.setOnClickListener { view: View? ->
            view?.findNavController()
                ?.navigate(R.id.action_navigation_profile_to_changePaymentDetails)
        }
    }
}