package com.example.cp3407_assignment.ui.home

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.checkerframework.checker.units.qual.s
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private val db = Firebase.firestore
    private val dogDBRef = db.collection("Dogs")

    private lateinit var mAdapter: ImageAdapter

    private val binding get() = _binding!!
    private var mUploads = ArrayList<Dog>()
    private lateinit var searchList: ArrayList<Dog>
    private var searchQuery = ""
    private lateinit var liveList: MutableLiveData<ArrayList<Dog>>


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

        if (savedInstanceState != null) { //TODO fix this
            //binding.searchBar.clearFocus()
            //binding.searchBar.setQuery("this is a test", true)
            searchQuery = savedInstanceState.getString("search_query").toString()
            Toast.makeText(
                context,
                savedInstanceState.getString("search_query") + " not NULL",
                Toast.LENGTH_SHORT
            ).show()
//        if (mUploads.isEmpty()) {
//
//        }

        }
        loadDogs()

        liveList = MutableLiveData()
        liveList.observe(viewLifecycleOwner) {
            //Toast.makeText(context,"detect change", Toast.LENGTH_SHORT).show()
            val mRecyclerView = binding.recyclerView
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.layoutManager = LinearLayoutManager(context)
            //mAdapter.searchDataList(it)
            updateRecyclerView(mRecyclerView, it)
            //updateRecyclerView(mRecyclerView)
        }

//        val mRecyclerView = binding.recyclerView
//        mRecyclerView.setHasFixedSize(true)
//        mRecyclerView.layoutManager = LinearLayoutManager(context)

//        mUploads = ArrayList()

        mAdapter = context?.let { ImageAdapter(it, mUploads) }!!
        //updateRecyclerView(mRecyclerView)

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("search_query", binding.searchBar.query.toString())
//        outState.putString("search_query", searchQuery)
//        outState.putString("search_query", "searchQuery")
        super.onSaveInstanceState(outState)
    }


//    override fun onActivityCreated(@Nullable savedInstanceState: Bundle) {
//        super.onActivityCreated(savedInstanceState)
//        title = savedInstanceState.getString(TITLE)
//        rating = savedInstanceState.getDouble(RATING).toInt()
//        year = savedInstanceState.getInt(YEAR)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if (binding.searchBar.query.toString() != "") {
//            if (savedInstanceState != null) {
//                search(savedInstanceState.getString("search_query")?.let { search(it) }.toString())
//            }
//            Toast.makeText(
//                context,
//                "search bar is NOT empty" + binding.searchBar.query.toString() + "---",
//                Toast.LENGTH_LONG
//            ).show()
        //}




        //searchbar logic
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                binding.recyclerView.scrollToPosition(0)
                searchQuery = newText
                search(newText)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        //reset the page if the "X" button is pushed in the search bar
        val closeBtn: View =
            binding.searchBar.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeBtn.setOnClickListener {
            //binding.searchBar.setQuery("", false) // reset Query text to be empty without submition
            binding.searchBar.isIconified = true // Replace the x icon with the search icon
            binding.textViewSearchResults.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.recyclerView.scrollToPosition(0)
            mAdapter.searchDataList(mUploads)
        }

    }

    private fun search(newText: String) {
        //onSaveInstanceState()
        createSpinner()
        searchList(newText)
        binding.textViewSearchResults.visibility = View.VISIBLE
        binding.spinner.visibility = View.VISIBLE
        binding.searchBar.clearFocus()
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

    private fun sortSearchList(dataList: ArrayList<Dog>, position: Int) {
        when (position) {
            0 -> dataList.sortBy { it.cost?.toFloat() } //price low to high
            1 -> dataList.sortByDescending { it.cost?.toFloat() } //price high to low
            2 -> dataList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.doggo_breed!! }) //A-Z
            3 -> dataList.sortByDescending { it.doggo_breed!! } //Z-A
            else -> { //popular
                dataList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.doggo_review!! })
            }
        }
        mAdapter.searchDataList(dataList)
    }

    private fun loadDogs() {
        // update the recyclerview
        val doggos = ArrayList<Dog>()
        dogDBRef.get().addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val dog = documentSnapshot.toObject<Dog>()
                    doggos.add(dog)
                    liveList.value = doggos
                }
            }
        //return doggos
    }

    private fun updateRecyclerView(mRecyclerView: RecyclerView, doggoList:  ArrayList<Dog>) {
        mAdapter = context?.let { ImageAdapter(it, doggoList) }!!
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
                        "end_date" to model.hire_end_date,
                        "search_query" to binding.searchBar.query.toString()
                    )
                findNavController().navigate(
                    R.id.action_navigation_home_to_doggoInformation,
                    bundle
                )
            }
        })
    }

    private fun searchList(text: String) {
        searchList = ArrayList()
        liveList.value?.forEach {
            if (it.description?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true || it.doggo_breed?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true || it.doggo_name?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(it)
                //liveList.value = searchList
            }
        }
//        for (dog in liveList) {
//            if (dog.description?.lowercase()
//                    ?.contains(text.lowercase(Locale.getDefault())) == true || dog.doggo_breed?.lowercase()
//                    ?.contains(text.lowercase(Locale.getDefault())) == true || dog.doggo_name?.lowercase()
//                    ?.contains(text.lowercase(Locale.getDefault())) == true
//            ) {
//                searchList.add(dog)
//                liveList.value = searchList
//            }
//        }
        binding.textViewSearchResults.text =
            getString(R.string.search_results_number, searchList.size.toString())
        //mUploads = searchList
        mAdapter.searchDataList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        //binding.searchBar.setQuery("", false) //clear the searchbar of old user input

        //binding.searchBar.setQuery(arguments?.getString("search_query"), true)

        // this stops the back button so that the user cant go back to the login screen
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }

        if (searchQuery != "") {
            //binding.searchBar.isIconified = true
            //binding.searchBar.onActionViewExpanded()
            binding.searchBar.post { binding.searchBar.setQuery(searchQuery, false) }
            //binding.searchBar.setQuery(searchQuery, true)
            //binding.searchBar.isFocusable = false
            search(searchQuery)
        }

    }


}