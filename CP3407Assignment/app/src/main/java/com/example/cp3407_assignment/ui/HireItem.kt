package com.example.cp3407_assignment.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHireItemBinding

class HireItem : Fragment() {

    private val viewModel: HireItemViewModel by viewModels()
    private lateinit var binding: FragmentHireItemBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate layout for fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hire_item, container, false)

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

        binding.name.setOnKeyListener{view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.description.setOnKeyListener{view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
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

    @Override
    override fun onPause() {
        super.onPause()
    }
}