package com.example.cp3407_assignment.ui.current_hire

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cp3407_assignment.R

class Currently_Hiring : Fragment() {

    companion object {
        fun newInstance() = Currently_Hiring()
    }

    private lateinit var viewModel: CurrentlyHiringViewModel2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currently__hiring, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentlyHiringViewModel2::class.java)
        // TODO: Use the ViewModel
    }

}