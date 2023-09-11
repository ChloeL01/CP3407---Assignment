package com.example.cp3407_assignment.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.User
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    //private val TAG = "MainActivity"
    private lateinit var  firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    private val db = Firebase.firestore
    private val dogDBRef = db.collection("Dogs")

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

        storageReference = FirebaseStorage.getInstance().reference.child("Storage")
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.buttonChooseImage.setOnClickListener {
            resultLauncher.launch("image/*")
        }
        binding.buttonUploadImage.setOnClickListener {
            uploadImage()
        }

        return root
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mRecyclerView = binding.recyclerView

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val mUploads = ArrayList<Dog>()

        // update the recyclerview
        dogDBRef.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val dog = documentSnapshot.toObject<Dog>()
                    mUploads.add(dog)
                }
                val mAdapter = context?.let { ImageAdapter(it, mUploads) }
                mRecyclerView.adapter = mAdapter
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImage() {
        // call this to upload the doggo to the server
        storageReference = storageReference.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageReference.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val upload = Dog( //TODO replace with user input
                            "new doggo name",
                            "new doggo breed",
                            "new doggo description goes here",
                            "new doggo hire date start",
                            "new doggo hire date end",
                            "new doggo cost",
                            "new doggo good boi points",
                            "owner id",
                            "owner contact",
                            uri.toString()
                        )
                        firebaseFirestore.collection("Dogs").add(upload).addOnCompleteListener { firestoreTask ->

                            if (firestoreTask.isSuccessful){
                                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                            //binding.imageView.setImageResource(R.drawable.vector) TODO replace imageview with the doggo pic
                        }
                    }
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    //binding.imageView.setImageResource(R.drawable.vector) TODO replace imageview with a 'fail to upload' pic
                }
            }
        }
    }
}