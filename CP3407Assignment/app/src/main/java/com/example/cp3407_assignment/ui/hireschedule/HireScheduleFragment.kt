package com.example.cp3407_assignment.ui.hireschedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHireScheduleBinding
import com.example.cp3407_assignment.ui.home.ImageAdapter
import com.google.android.play.integrity.internal.z
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class HireScheduleFragment : Fragment(), CalenderAdapter.OnItemListener {

    private lateinit var calenderRecyclerView: RecyclerView
    private lateinit var dateSelection: LocalDate
    private lateinit var binding: FragmentHireScheduleBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hire_schedule, container, false)

        binding.confirmButton.setOnClickListener{
            val bundle =
                bundleOf(
                    "doggo_name" to arguments?.getString("doggo_name"),
                    "doggo_breed" to arguments?.getString("doggo_breed"),
                    "imageUrl" to arguments?.getString("imageUrl"),
                    "description" to arguments?.getString("description"),
                    "reviews" to arguments?.getString("reviews"),
                    "start_date" to arguments?.getString("start_date"),
                    "end_date" to arguments?.getString("end_date"),
                    "cost" to arguments?.getString("cost"),
                    "owner_id" to arguments?.getString("owner_id"),
                    "owner_contact" to arguments?.getString("owner_contact"),
                    "hiree" to arguments?.getString("hiree")
                )
            findNavController().navigate(R.id.action_hireScheduleFragment_to_navigation_cart, bundle)
        }
        calenderRecyclerView = binding.calenderRecyclerView
        initialiseWidgets()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateSelection = LocalDate.now()
        }
        selectMonthView()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectMonthView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.textMonthYear.text = dateForMonthYear(dateSelection)
        }
        val daysInMonth: ArrayList<String> = daysInMonthArray(dateSelection)
//        val daysInMonth: ArrayList<String> = arrayListOf("1", "2", "3")
        calenderRecyclerView.layoutManager = GridLayoutManager(context, 7)
//        calenderRecyclerView?.adapter = CalenderAdapter(daysInMonth, this)

        val adapter = context?.let { CalenderAdapter(daysInMonth, this) }!!
        calenderRecyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray: ArrayList<String> = ArrayList()

        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()

        val firstInMonth: LocalDate = dateSelection.withDayOfMonth(1)
        val dayOfWeek: Int = firstInMonth.dayOfWeek.value

        for (i in 0 until 42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add(String.format((i - dayOfWeek).toString()))
            }
        }
        return daysInMonthArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateForMonthYear(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
        return date.format(formatter)
    }

    private fun initialiseWidgets() {
        binding.calenderRecyclerView
        binding.textMonthYear
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickPreviousMonth() {
        dateSelection = dateSelection.minusMonths(1)
        selectMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickNextMonth() {
        dateSelection = dateSelection.plusMonths(1)
        selectMonthView()
    }

    fun onClickDateConfirmation() {
        //TODO attempt to make dates in recyclerview clickable and assignable for recording on database

        //TODO when button is clicked, assign selected dates to database
    }

    fun onClickDateSelection() {
        //TODO make items in recycler view clickable and associated dates stored within array
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, dayText: String) {
        if (dayText == "") {
            val message: String =
                "This date has been selected: " + dayText + " " + dateForMonthYear(dateSelection)
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        }
    }

}