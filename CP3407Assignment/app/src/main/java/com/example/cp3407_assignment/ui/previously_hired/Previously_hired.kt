package com.example.cp3407_assignment.ui.previously_hired

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
import com.example.cp3407_assignment.databinding.FragmentPreviouslyHiredBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Previously_hired : Fragment() {

    private var _binding: FragmentPreviouslyHiredBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    private val dogDBRef = db.collection("Dogs")
    private val userDBRef = db.collection("Users")
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val PreviouslyHiredViewModel =
            ViewModelProvider(this).get(PreviouslyHiredViewModel::class.java)

        _binding = FragmentPreviouslyHiredBinding.inflate(inflater, container, false)
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
        firebaseAuth = Firebase.auth

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(
                context,
                "No user signed in",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            val myRecyclerView = binding.PreviouslyHiredRecycle

            myRecyclerView.setHasFixedSize(true)
            myRecyclerView.layoutManager = LinearLayoutManager(context)

            val mUploads = ArrayList<Dog>()
            val previousDoggos = ArrayList<Dog>()
            userDBRef.get()


            dogDBRef.get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (documentSnapshot in queryDocumentSnapshots) {
                        val dog = documentSnapshot.toObject<Dog>()
                        mUploads.add(dog)
                    }

                    for (dog in mUploads) {
                        val currentUserData = firebaseAuth.currentUser
                        val userUid = currentUserData?.uid


                        if (dog.hiree == userUid.toString()) {
                            previousDoggos.add(dog)
                        }
                    }
                    if (previousDoggos.size.toString() == "0") {
                        Toast.makeText(
                            context,
                            "You are not currently hiring any dogs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val mAdapter =
                        context?.let { PreviouslyHiredItemAdapter(it, previousDoggos) }
                    myRecyclerView.adapter = mAdapter

                    mAdapter?.setOnClickListener(object :
                        PreviouslyHiredItemAdapter.OnClickListener {
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
                                R.id.action_previously_hired_to_doggoInformation,
                                bundle
                            )
                        }
                    })

                }


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

