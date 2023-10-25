package com.example.cp3407_assignment.ui.home

import android.app.ActionBar
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHomeBinding
import com.example.cp3407_assignment.ui.CreateDogBundle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


open class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private val db = Firebase.firestore
    private val dogDBRef = db.collection("Dogs")

    private lateinit var mAdapter: ImageAdapter
    private lateinit var mRecyclerView: RecyclerView

    private val binding get() = _binding!!

    private var mUploads = ArrayList<Dog>()
    private var searchList: ArrayList<Dog> = arrayListOf()
    private var dateFilterLst: ArrayList<Dog> = arrayListOf()
    private var dateFilterText = ""
    private var searchQuery = ""

    private var recyclerListPosition: Int = 0

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        storageReference = FirebaseStorage.getInstance().reference.child("Storage")
        firebaseFirestore = FirebaseFirestore.getInstance()

        mRecyclerView = binding.recyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val sharedPref: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)

        if (mUploads.isEmpty()) {
//        if (search == "[]") {
            //check if the shared preference is empty
            val editor = sharedPref.edit()
            editor.putString("searchList", "[]")
            editor.apply()
            searchQuery = ""
            clearPage()
            loadDogs()
        }
        val search = sharedPref.getString("searchList", "[]")
        val dateFilterSearch = sharedPref.getString("dateFilerList", "[]")
        if (search != "[]") {
            loadPreference()
            val list = if (dateFilterSearch != "[]") {
                dateFilterLst
            } else {
                searchList
            }
            binding.button2.text = sharedPref.getString("dateFilterText", "Select Date")
            updateSearchCount(list)
            searchQuery = sharedPref.getString("searchText", "").toString()
            showSpinner()
            createSpinner(list)
            homeViewModel.setDoggos(list)
        }
        binding.searchBar.post { binding.searchBar.setQuery(searchQuery, false) }
        homeViewModel.getDoggos().observe(viewLifecycleOwner) {
            //Toast.makeText(context,"detect change", Toast.LENGTH_SHORT).show()
            updateRecyclerView(it)
            (mRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                recyclerListPosition,
                0
            )
        }

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelable("Layout manager", mRecyclerView.layoutManager?.onSaveInstanceState())
        savePreference()
        super.onSaveInstanceState(outState)
    }

    private fun loadPreference() {
        val sharedPref: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        val gson = Gson()
        val datejson = sharedPref.getString("dateFilerList", "[]")
        val json = sharedPref.getString("searchList", "[]")
        val type: Type = object : TypeToken<ArrayList<Dog>>() {}.type
        if (searchList.isEmpty()) {
            searchList = ArrayList()
        }
        if (dateFilterLst.isEmpty()) {
            dateFilterLst = ArrayList()
        }
        dateFilterLst = gson.fromJson(datejson, type)
        searchList = gson.fromJson(json, type)
    }

    private fun savePreference() {
        val sharedPref: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val datejson: String = gson.toJson(dateFilterLst)
        val json: String = gson.toJson(searchList)
        editor.putString("dateFilterText", dateFilterText)
        editor.putString("dateFilerList", datejson)
        editor.putString("searchList", json)
        editor.putString("searchText", searchQuery)

        editor.apply()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //swipe down from the top to update the list
        binding.container.setOnRefreshListener {
            binding.container.isRefreshing = false
            clearPage()
            loadDogs()
            homeViewModel.setDoggos(mUploads)
        }

        //searchbar logic
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                binding.recyclerView.scrollToPosition(0)
                searchQuery = newText
                binding.button2.text = getString(R.string.select_date)
                showSpinner()
                searchList(newText)
                createSpinner(searchList)
                //homeViewModel.doggoList.value = searchList
                recyclerListPosition = 0
                homeViewModel.setDoggos(searchList)
                //updateRecyclerView(searchList)
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
            //binding.searchBar.post { binding.searchBar.setQuery("", true) }
            binding.searchBar.isIconified = true // Replace the x icon with the search icon
            binding.button2.text = getString(R.string.select_date)
            clearPage()
            loadDogs()
            homeViewModel.setDoggos(mUploads)
        }

        binding.button2.setOnClickListener {
            val materialDatePicker = materialDatePicker1()
            materialDatePicker.show(childFragmentManager, "DatePicker")
            materialDatePicker.addOnPositiveButtonClickListener { pair ->
                materialDatePicker(pair)
            }
        }

    }

    private fun materialDatePicker1(): MaterialDatePicker<Pair<Long, Long>> {
        return MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select date")
            .setPositiveButtonText("Submit")
            .build()
    }

    private fun materialDatePicker(pair: Pair<Long, Long>) {
//        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDate = pair.first
//                val startDate = Date(pair.first)
        val formattedStartDate = simpleDateFormat.format(startDate)
//                val endDate = Date(pair.second)
        val endDate = pair.second
        val difference = endDate - startDate
//        val daysDiff = TimeUnit.MILLISECONDS.toDays(difference)
        val formattedEndDate = simpleDateFormat.format(endDate)

        binding.button2.text = "$formattedStartDate - $formattedEndDate"
        applyDateFilter(startDate, endDate)
    }


    private fun clearPage() {
        binding.searchBar.setQuery("", true) // reset Query text to be empty without submition
        searchQuery = ""
        searchList = ArrayList()
        hideSpinner()
        recyclerListPosition = 0
    }

    private fun showSpinner() {
        binding.textViewSearchResults.visibility = View.VISIBLE
        binding.spinner.visibility = View.VISIBLE
        binding.button2.visibility = View.VISIBLE
        binding.searchBar.clearFocus()
    }

    private fun hideSpinner() {
        binding.textViewSearchResults.visibility = View.GONE
        binding.spinner.visibility = View.GONE
        binding.button2.visibility = View.GONE
        binding.searchBar.clearFocus()
    }


    private fun createSpinner(searchList: ArrayList<Dog>) {
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
        homeViewModel.setDoggos(dataList)
    }


    private fun loadDogs() {
        // get doggos from the database
        val doggos = ArrayList<Dog>()
        dogDBRef.get().addOnSuccessListener { queryDocumentSnapshots ->
            for (documentSnapshot in queryDocumentSnapshots) {
                val dog = documentSnapshot.toObject<Dog>()
                doggos.add(dog)
                //Log.w("Load dogs", "Success")
            }
            mUploads = doggos
            homeViewModel.setDoggos(mUploads)
        }.addOnFailureListener { exception ->
            Log.w("Load dogs", "Error getting documents: ", exception)
        }
    }

    private fun updateRecyclerView(doggoList: ArrayList<Dog>) {
        mRecyclerView.scrollToPosition(0)
        mAdapter = context?.let { ImageAdapter(it, doggoList) }!!
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnClickListener(object : ImageAdapter.OnClickListener {
            override fun onClick(position: Int, model: Dog) {
                val bundle = CreateDogBundle().createBundle(model)
                savePreference()
                findNavController().navigate(
                    R.id.action_navigation_home_to_doggoInformation,
                    bundle
                )
            }
        })
    }

    private fun searchList(text: String) {
        searchList = ArrayList()
        //search on description, name, or breed
        for (dog in mUploads) {
            if (dog.description?.lowercase()?.contains(text.lowercase(Locale.getDefault())) == true
                || dog.doggo_breed?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
                || dog.doggo_name?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dog)
            }
        }
        updateSearchCount(searchList)
    }

    private fun applyDateFilter(start_date_filter: Long, end_date_filter: Long) {
        val datesList = ArrayList<Dog>()

        for (dog in searchList) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val start_date = dog.hire_start_date?.let { sdf.parse(it) }
            val end_date = dog.hire_end_date?.let { sdf.parse(it) }
            if (start_date_filter <= end_date!!.time && end_date_filter >= start_date!!.time) {
                datesList.add(dog)
                homeViewModel.setDoggos(datesList)
            }
        }
        dateFilterText = binding.button2.text.toString()
        dateFilterLst = datesList
        updateSearchCount(datesList)
        createSpinner(datesList)
    }

    private fun updateSearchCount(searchList: ArrayList<Dog>) {
        binding.textViewSearchResults.text =
            getString(R.string.search_results_number, searchList.size.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // this stops the back button so that the user cant go back to the login screen
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    /* private fun uploadImage() { //TODO use this to upload a doggo to the database
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
                             "hiree",
                             uri.toString()
                         )
                         firebaseFirestore.collection("Dogs").add(upload)
                             .addOnCompleteListener { firestoreTask ->

                                 if (firestoreTask.isSuccessful) {
                                     Toast.makeText(
                                         context,
                                         "Uploaded Successfully",
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 } else {
                                     Toast.makeText(
                                         context,
                                         firestoreTask.exception?.message,
                                         Toast.LENGTH_SHORT
                                     ).show()
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
 */
    override fun onPause() {
        super.onPause()
        recyclerListPosition =
            (mRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    }
}