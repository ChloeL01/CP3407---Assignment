package com.example.cp3407_assignment.ui.current_hire

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cp3407_assignment.R

class CurrentHireFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentHireFragment()
    }

    private lateinit var viewModel: CurrentHireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_hire, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentHireViewModel::class.java)
        // TODO: Use the ViewModel
    }

}