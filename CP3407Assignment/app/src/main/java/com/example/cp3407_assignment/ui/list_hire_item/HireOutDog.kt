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
import androidx.navigation.NavAction
import androidx.navigation.NavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHireOutDogBinding

class HireOutDog : Fragment() {

    private lateinit var layout: View
    private lateinit var binding: FragmentHireOutDogBinding
    private val listDogViewModel: HireOutDogViewModel by viewModels()

    private lateinit var requestPermission: ActivityResultLauncher<String>
    private lateinit var pickMultipleVisualMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private var selectedBreed: String? = null
    private val uris: MutableList<Uri> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate layout for fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hire_out_dog, container, false)

        binding.dogHireViewModel = listDogViewModel
        layout = binding.listToHireConstraint



        // Register permission callback which handles user's response to system permission dialog. Saves the return value.
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.i("Permission: ", "Granted")
                } else {
                    Log.i("Permission: ", "Denied")
                }
            }

        pickMultipleVisualMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) {
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        // Populate dog breed spinner
        val spinner: Spinner = binding.breedSpinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.dogBreeds,
            R.layout.dogbreed_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.dogbreed_spinner_dropdown_item)
            spinner.adapter = adapter
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

        binding.uploadImageButton.setOnClickListener {
            onClickRequestPermission()
            pickMultipleVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Save listing information
        binding.listDogButton.setOnClickListener {
            onSubmitListing()
        }
    }

    /**
     * Permissions to access camera and/or gallery
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted continue action or workflow in app.

            }

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted continue to gallery
            }


            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            ) -> {

            }
            else -> {
                requestPermission.launch(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            }
        }
    }

    /**
     * Save the changes that will be sent to the database to list a dog to be available for hire
     */
    private fun onSubmitListing() {
        if (listDogViewModel.dogName.value == null || listDogViewModel.description.value == null || listDogViewModel.cost.value == 0.0){
           Toast.makeText(context, "Fields cannot be blank", Toast.LENGTH_LONG).show() // This can be implemented better when with Material components. Later job :)
        }

        listDogViewModel.dogName.value = binding.name.toString()
        listDogViewModel.description.value = binding.description.toString()
        selectedBreed = binding.breedSpinner.onItemSelectedListener.toString()

        // Checking contact type selected
        var contactType = ""
        if (binding.emailCheckbox.isChecked && binding.phoneCheckbox.isChecked) {
            contactType = "both"
        } else if (binding.emailCheckbox.isChecked) {
            contactType = "email"
        } else if (binding.phoneCheckbox.isChecked) {
            contactType = "phone"
        } else {
            Toast.makeText(context, "Please select a contact type", Toast.LENGTH_SHORT).show()
        }

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
        requestPermission.unregister()
        pickMultipleVisualMedia.unregister()
    }
}