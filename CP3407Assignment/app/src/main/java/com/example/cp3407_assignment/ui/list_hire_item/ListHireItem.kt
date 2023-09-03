package com.example.cp3407_assignment.ui.list_hire_item

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentListHireItemBinding

class ListHireItem : Fragment() {

    private lateinit var binding: FragmentListHireItemBinding
    private val listItemViewModel: ListHireItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate layout for fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_item, container, false)

        binding.listItemViewModel = listItemViewModel

        return binding.root
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
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
            uploadImages()
        }

        // Save listing information
        binding.listDogButton.setOnClickListener {
            onSubmitListing()
        }
    }

    private fun uploadImages() {
//        val pickPhoto = registerForActivityResult(
//            ActivityResultContracts.PickVisualMedia()
//        ) { uri ->
//            if (uri == null) {
//                Log.d("PhotoPicker", "Selected URI: $uri")
//            } else {
//                Log.d("PhotoPicker", "No media selected")
//            }
//        }
//
//        pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        val fileType = "image/jpeg"
//        pickPhoto.launch(
//            PickVisualMediaRequest(
//                ActivityResultContracts.PickVisualMedia.SingleMimeType(
//                    fileType
//                )
//            )
//        )
    }


    private fun onSubmitListing() {
        // TODO
        // Contact check boxes
        // Save images ??
        // Save to database

        listItemViewModel.dogName.value = binding.name.toString()
        listItemViewModel.description.value = binding.description.toString()

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

        //TODO: Need to implement into dotabase
//        Name
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
}