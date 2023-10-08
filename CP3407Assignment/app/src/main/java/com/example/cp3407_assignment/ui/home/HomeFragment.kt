package com.example.cp3407_assignment.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private val db = Firebase.firestore
    private val dogDBRef = db.collection("Dogs")

    private lateinit var mAdapter: ImageAdapter

    private val binding get() = _binding!!
    private lateinit var mUploads: ArrayList<Dog>
    private lateinit var searchList: ArrayList<Dog>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        storageReference = FirebaseStorage.getInstance().reference.child("Storage")
        firebaseFirestore = FirebaseFirestore.getInstance()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mRecyclerView = binding.recyclerView

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        mUploads = ArrayList()

        mAdapter = context?.let { ImageAdapter(it, mUploads) }!!

        loadDogs(mUploads, mRecyclerView)

        //searchbar logic
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                createSpinner()
                searchList(newText, mUploads)
                binding.textViewSearchResults.visibility = View.VISIBLE
                binding.spinner.visibility = View.VISIBLE
                binding.searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun createSpinner() {
        //spinner setup
        val filterOptions = resources.getStringArray(R.array.Filter_options)

        //load spinner options
        val spinner = binding.spinner
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, filterOptions
            )
        }
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                sortSearchList(searchList, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun sortSearchList(dataList: ArrayList<Dog>, position: Int){
        when (position) {
            0 -> dataList.sortBy { it.cost?.toFloat()}
            1 -> dataList.sortByDescending { it.cost?.toFloat() }
            2 -> dataList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.doggo_breed!! })
            3 -> dataList.sortByDescending{ it.doggo_breed!! }
            else -> {
                dataList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.doggo_review!! })
            }
        }
        mAdapter.searchDataList(dataList)
    }

    private fun loadDogs(mUploads: ArrayList<Dog>, mRecyclerView: RecyclerView) {
        // update the recyclerview
        dogDBRef.orderBy("doggo_breed", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val dog = documentSnapshot.toObject<Dog>()
                    mUploads.add(dog)
                }
                mAdapter = context?.let { ImageAdapter(it, mUploads) }!!
                mRecyclerView.adapter = mAdapter

                mAdapter.setOnClickListener(object : ImageAdapter.OnClickListener {
                    override fun onClick(position: Int, model: Dog) {
                        val bundle =
                            bundleOf(
                                "doggo_name" to model.doggo_name,
                                "doggo_breed" to model.doggo_breed,
                                "imageUrl" to model.imageUrl,
                                "description" to model.description,
                                "reviews" to model.doggo_review,
                                "start_date" to model.hire_start_date,
                                "end_date" to model.hire_end_date
                            )
                        findNavController().navigate(
                            R.id.action_navigation_home_to_doggoInformation,
                            bundle
                        )
                    }
                })
            }
    }

    fun searchList(text: String, dataList: ArrayList<Dog>) {
        searchList = ArrayList<Dog>()
        for (dataClass in dataList) {
            if (dataClass.description?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        binding.textViewSearchResults.text =
            getString(R.string.search_results_number, searchList.size.toString())
        mAdapter.searchDataList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.searchBar.setQuery("", false) //clear the searchbar of old user input

        // this stops the back button so that the user cant go back to the login screen
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }


}