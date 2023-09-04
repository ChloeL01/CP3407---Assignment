package com.example.cp3407_assignment.ui.list_hire_item

import android.Manifest
import android.app.DownloadManager.Request
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentListHireItemBinding
import com.google.android.material.snackbar.Snackbar

class ListHireItem : Fragment() {

    private lateinit var layout: View
    private lateinit var binding: FragmentListHireItemBinding
    private lateinit var requestPermission: ActivityResultLauncher<String>
    private val listItemViewModel: ListHireItemViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate layout for fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_item, container, false)

        binding.listItemViewModel = listItemViewModel
        layout = binding.listToHireConstraint

//        Register permission callback which handles user's response to
//        system permission dialog. Saves the return value.
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.i("Permission: ", "Granted")
                } else {
                    Log.i("Permission: ", "Denied")
                }
            }

        return binding.root
    }

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
        }

//        Save listing information
        binding.listDogButton.setOnClickListener {
            onSubmitListing()
        }
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted continue action or workflow in app.
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            ) -> {

            }

            else -> {
                requestPermission.launch(
                    Manifest.permission.CAMERA
                )
            }

        }
    }

    private fun onSubmitListing() {
        // TODO
        // Contact check boxes
        // Save images ??
        // Save to database
        // Need to track current user who is logged in

        listItemViewModel.dogName.value = binding.name.toString()
        listItemViewModel.description.value = binding.description.toString()

//        Checking contact type selected
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
//        User name
//        Description
//        Contact type
//        Images
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
    }
}