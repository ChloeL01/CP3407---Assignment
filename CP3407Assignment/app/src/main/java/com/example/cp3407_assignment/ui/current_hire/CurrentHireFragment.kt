package com.example.cp3407_assignment.ui.current_hire

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentCurrentHireBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

class CurrentHireFragment : Fragment() {

    private var _binding: FragmentCurrentHireBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    private val dogDBRef = db.collection("Dogs")

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        /*val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Toast.makeText(
                context,
                "How You doin'",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "No user here ",
                Toast.LENGTH_SHORT
            ).show()
        }*/

        /*
                ///////////////////////////////////////////////////////////
                val db = FirebaseFirestore.getInstance()
                val currentUser = firebaseAuth.currentUser?.uid
                val userRef = db.collection("Users").document(currentUser!!)
                Toast.makeText(
                    context,
                    "Hello There "+currentUser,
                    Toast.LENGTH_SHORT
                ).show()

                userRef.addSnapshotListener { docSnapshot, firebaseFireStoreException ->
                    if (firebaseFireStoreException != null) {
                        Toast.makeText(
                            context,
                            "Not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (docSnapshot != null && docSnapshot.exists()) {
                        val userName = docSnapshot.getString("username")
                        Toast.makeText(
                            context,
                            "Current user username: "+ userName,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Current user username: null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        */
        val CurrentHireViewModel =
            ViewModelProvider(this).get(CurrentHireViewModel::class.java)

        _binding = FragmentCurrentHireBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storageReference = FirebaseStorage.getInstance().reference.child("Storage")
        firebaseFirestore = FirebaseFirestore.getInstance()

        return root

    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    )
    {
        imageUri = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myRecyclerView = binding.CurrentHireRecycle

        myRecyclerView.setHasFixedSize(true)
        myRecyclerView.layoutManager = LinearLayoutManager(context)

        val mUploads = ArrayList<Dog>()

        dogDBRef.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val dog = documentSnapshot.toObject<Dog>()
                    mUploads.add(dog)
                }
                val mAdapter = context?.let { CurrentlyHiringItemAdapter(it, mUploads) }
                myRecyclerView.adapter = mAdapter


                //db.collection("Dogs").document(Hiree).get()
                // .addOnSuccessListener { documentSnapshot ->
                //  if (documentSnapshot.exists()) {
                //     val hiree = documentSnapshot.getString(Hiree)
                //   Toast.makeText(
                //       context,
                //       "Hello World",
                //       Toast.LENGTH_SHORT
                //   ).show()
                //  }}

                //if (hiree == "Keziah") {
                mAdapter?.setOnClickListener(object :
                    CurrentlyHiringItemAdapter.OnClickListener {
                    override fun onClick(position: Int, model: Dog) {
                        val bundle =
                            bundleOf(
                                "doggo_name" to model.doggo_name,
                                "imageUrl" to model.imageUrl,
                                "start_date" to model.hire_start_date,
                                "end_date" to model.hire_end_date,
                                "breed" to model.doggo_breed
                            )
                        findNavController().navigate(
                            R.id.action_currently_Hiring_to_doggoInformation,
                            bundle
                        )
                    }
                })
                // }

            }
            }
/*
    override fun onStart(){
        super.onStart()
        firebaseAuth = Firebase.auth
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(
                context,
                "No user signed in",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Hello There ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }*/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}