package com.example.cp3407_assignment.ui.list_hire_item

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHireOutDogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class HireOutDog : Fragment() {

    private lateinit var layout: View
    private lateinit var binding: FragmentHireOutDogBinding
    private val listDogViewModel: HireOutDogViewModel by viewModels()

    private lateinit var pickVisualMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val uris: MutableList<Uri> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate layout for fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hire_out_dog, container, false)

        binding.dogHireViewModel = listDogViewModel
        layout = binding.listToHireScroll

        pickVisualMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { selectedUris ->
                if (selectedUris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                    uris.clear()
                    uris.addAll(selectedUris)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted. Continue the action or workflow in your app
                    Log.i("Permission: ", "Granted")
                    pickVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                } else {
                    Log.i("Permission: ", "Denied")
                }
            }

        // Populate dog breed spinner
        val breedSpinner: Spinner = binding.breedSpinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.dogBreeds,
            R.layout.dogbreed_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.dogbreed_spinner_dropdown_item)
            breedSpinner.adapter = adapter
        }

        // Populate contact spinner
        val contactSpinner: Spinner = binding.contactSpinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.contactTypes,
            R.layout.contact_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.contact_spinner_dropdown_item)
            contactSpinner.adapter = adapter
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Override
    override fun onResume() {
        super.onResume()

        binding.lifecycleOwner = this

        binding.name.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.description.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

        // Save listing information
        binding.listDogButton.setOnClickListener {
            onSubmitListing()
        }

        binding.dateRange.setOnClickListener {
            getDateRange()
        }

        binding.uploadImageButton.setOnClickListener {
            onClickRequestPermission()
        }
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            ActivityCompat. shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )  -> {
                Snackbar.make(layout, R.string.permission_required, Snackbar.LENGTH_INDEFINITE).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun getDateRange() {
        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select dog availability")
                .build()
        datePicker.show(childFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            listDogViewModel.startDate.value = formatDate.format(Date(startDate))
            listDogViewModel.endDate.value = formatDate.format(Date(endDate))

            val selectedDateRange =
                "${listDogViewModel.startDate.value} - ${listDogViewModel.endDate.value}"
            binding.dateRange.text = selectedDateRange
        }
    }

    /**
     * Save the changes that will be sent to the database to list a dog to be available for hire
     */
    private fun onSubmitListing() {

        if (listDogViewModel.dogName.value == null || listDogViewModel.description.value == null || listDogViewModel.location.value == null || listDogViewModel.cost.value == 0.0) {
            Toast.makeText(context, "Fields cannot be blank", Toast.LENGTH_LONG)
                .show() // This can be implemented better when with Material components. Later job :)
        }

        listDogViewModel.dogName.value = binding.name.toString()
        listDogViewModel.description.value = binding.description.toString()
        listDogViewModel.location.value = binding.location.toString()

        listDogViewModel.breed.value = binding.breedSpinner.onItemSelectedListener.toString()
        listDogViewModel.contactType.value =
            binding.contactSpinner.onItemSelectedListener.toString()

//        listDogViewModel.startDate.value = binding.startDate.toString()
//        listDogViewModel.endDate.value = binding.endDate.toString()

        //TODO: Need to implement into database
//        TODO: User name
//        TODO: Description
//        TODO: Contact type
//        TODO: Images - no clue on how to do that yet....
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide keyboard
            val inputMethodManager =
                view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    @Override
    override fun onPause() {
        super.onPause()
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        pickVisualMediaLauncher.unregister()
    }
}