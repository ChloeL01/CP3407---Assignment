package com.example.cp3407_assignment.ui.home

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.User
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val TAG = "MainActivity"

    private val KEY_USER = "user"
    private val KEY_PASSWORD = "password"

    private lateinit var analytics: FirebaseAnalytics
    private val db = Firebase.firestore
    private val myRef = db.document("Users/My First User")
    private val dogDBRef = db.collection("Dogs")
    private var storageRef = Firebase.storage

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
        //_binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mRecyclerView = binding.recyclerView

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val mUploads = ArrayList<Dog>()

        dogDBRef.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val dog = documentSnapshot.toObject<Dog>()
                    //dog.setDocumentId(documentSnapshot.id)
                    val name: String? = dog.doggo_name
                    val imageUrl: String? = dog.imageUrl
                    mUploads.add(dog)
                }
                val mAdapter = context?.let { ImageAdapter(it, mUploads) }
                mRecyclerView.adapter = mAdapter
            }
    }

    override fun onStart() {
        super.onStart()
        // automatically updates
        myRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Toast.makeText(context, "Error while loading!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject<User>()
                val username = user?.username
                val password = user?.password

                binding.textHome.text = "Title: $username\nDescription: $password"
                Log.d(TAG, "Current data: ${snapshot.data}")

            } else {
                binding.textHome.text = ""
                Log.d(TAG, "Current data: null")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun saveUser() {
//        val username: String = binding.editTextUser.text.toString()
//        val password: String = binding.editTextPassword.text.toString()
//
//        val user = User(username, password)
//
//        db.collection("Users").document("My First User").set(user)
//            .addOnSuccessListener {
//                Toast.makeText(
//                    context,
//                    "User saved",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, e.toString())
//            }
//    }
//
//    private fun loadUser() {
//        db.collection("Users").document("My First User").get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val user = documentSnapshot.toObject<User>()
//                    val username = user?.username
//                    val password = user?.password
//
//                    binding.textHome.text = "Title: $username\nDescription: $password"
//                } else {
//                    Toast.makeText(context, "Document does not exist", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, e.toString())
//            }
//    }
//
//    private fun updateUser() {
//        val password = binding.editTextPassword.text.toString()
//        myRef.update(KEY_PASSWORD, password)
//    }
//
//    private fun deleteUser() {
//        myRef.delete()
//    }
}