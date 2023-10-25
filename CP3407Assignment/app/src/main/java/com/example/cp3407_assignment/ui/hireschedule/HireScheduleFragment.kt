package com.example.cp3407_assignment.ui.hireschedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentHireScheduleBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class HireScheduleFragment : Fragment(), CalenderAdapter.OnItemListener {

    private var calenderRecyclerView: RecyclerView? = null
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
            findNavController().navigate(R.id.action_hireScheduleFragment_to_paymentFragment)
        }
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
        calenderRecyclerView?.layoutManager = GridLayoutManager(context, 7)
        calenderRecyclerView?.adapter = CalenderAdapter(daysInMonth, this)
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