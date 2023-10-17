package com.example.cp3407_assignment.ui.current_hire

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
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
import com.example.cp3407_assignment.ui.current_hire.CurrentlyHiringItemAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CurrentHireFragment : Fragment() {

    private var _binding: FragmentCurrentHireBinding? = null

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    private val db = Firebase.firestore
    private val dogDBRef = db.collection("Dogs")

    private val binding get() = _binding!!
    //companion object {
    //    fun newInstance() = CurrentHireFragment()
    //}

    //private lateinit var viewModel: CurrentHireViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_current_hire, container, false)

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
        //viewModel = ViewModelProvider(this).get(CurrentHireViewModel::class.java)
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

                mAdapter?.setOnClickListener(object : CurrentlyHiringItemAdapter.OnClickListener {
                    override fun onClick(position: Int, model: Dog) {
                        val bundle =
                            bundleOf(
                                "doggo_name" to model.doggo_name,
                                "imageUrl" to model.imageUrl,
                                //"description" to model.description,
                                //"reviews" to model.doggo_review,
                                "start_date" to model.hire_start_date,
                                "end_date" to model.hire_end_date
                            )

                    }
                })

            }
            }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}