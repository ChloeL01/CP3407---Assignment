package com.example.cp3407_assignment.ui.list_hire_item

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
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

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        listDogViewModel.imageUri = it
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate layout for fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hire_out_dog, container, false)

        binding.dogHireViewModel = listDogViewModel
        layout = binding.listToHireScroll

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

        binding.listDogButton.setOnClickListener {view: View? ->

            listDogViewModel.saveDogListing()

            if (listDogViewModel.isSuccessful){
                view?.findNavController()?.navigate(R.id.action_listHireItem_to_navigation_dogs4)
            }
        }

        binding.dateRange.setOnClickListener {
            getDateRange()
        }

        binding.uploadImageButton.setOnClickListener {
            onClickRequestPermission()
        }

        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not required
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                listDogViewModel.dogName.value = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Not required
            }
        })

        binding.description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not required
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                listDogViewModel.description.value = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Not required
            }
        })

        binding.location.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not required
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                listDogViewModel.location.value = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Not required
            }
        })

        binding.hireCost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not required
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                listDogViewModel.cost.value = sequence?.toString()?.toDoubleOrNull() ?: 0.0
            }

            override fun afterTextChanged(s: Editable?) {
                // Not required
            }
        })

        binding.breedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedBreed = parent?.getItemAtPosition(position).toString()
                listDogViewModel.breed.value = selectedBreed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO: Implement error checking if nothing selected
            }
        }

        binding.contactSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Not required
                    val selectedContact = parent?.getItemAtPosition(position).toString()
                    listDogViewModel.contactType.value = selectedContact
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // TODO: Implement error checking if nothing selected
                }
            }
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                resultLauncher.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Snackbar.make(layout, R.string.permission_required, Snackbar.LENGTH_INDEFINITE)
                    .show()
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
}