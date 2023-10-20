package com.example.cp3407_assignment.ui.hire_item

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cp3407_assignment.databinding.FragmentHireItemBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HireItem : Fragment() {

    private var _binding: FragmentHireItemBinding? = null
    private lateinit var viewModel: HireItemViewModel

    private val binding get() = _binding!!
    private lateinit var hireItemViewModel: HireItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHireItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        hireItemViewModel =
            ViewModelProvider(this)[HireItemViewModel::class.java]

        (activity as AppCompatActivity).supportActionBar?.title = "Hire details"

        val hire_start_date = arguments?.getString("start_date")
        val hire_end_date = arguments?.getString("end_date")
        val cost = arguments?.getString("cost")

        hireItemViewModel.startDateInfo.observe(viewLifecycleOwner) {
            binding.startDateInfo.text = hireItemViewModel.startDateInfo.value
        }

        hireItemViewModel.endDateInfo.observe(viewLifecycleOwner) {
            binding.endDateInfo.text = hireItemViewModel.endDateInfo.value
        }

        hireItemViewModel.timeInfo.observe(viewLifecycleOwner) {
            binding.timeInfo.text = hireItemViewModel.timeInfo.value
        }

        hireItemViewModel.costInfo.observe(viewLifecycleOwner) {
            binding.totalCost.text = hireItemViewModel.costInfo.value
        }

        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")

        val start_date = formatter.parse(hire_start_date)
        val end_date = formatter.parse(hire_end_date)

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        mcurrentTime.time = start_date
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(context,
            { _, hourOfDay, minuteOfDay ->
                hireItemViewModel.timeInfo.value =
                    String.format("Pickup time - %d : %d", hourOfDay, minuteOfDay)
                //selectedTime.setText(String.format("%d : %d", hourOfDay, minute))
            }, hour, minute, true
        )

        binding.availableTimes.setOnClickListener {
            val hour = binding.timePicker.hour
            val min = binding.timePicker.minute
            hireItemViewModel.timeInfo.value = "Pick up $hour : $min"
        }

        binding.availableDates.setOnClickListener {
            val constraints = CalendarConstraints.Builder()
                .setStart(start_date.time)
                .setEnd(end_date.time)
                .setValidator(DateValidatorPointBackward.before(end_date.time))
                .setValidator(DateValidatorPointForward.from(start_date.time))
                .build()

            val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(constraints)
                .setTitleText("Select date")
                .setPositiveButtonText("Submit")
                .build()

            materialDatePicker.show(childFragmentManager, "DatePicker")

            materialDatePicker.addOnPositiveButtonClickListener {pair ->
                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
                val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
                val startDate = pair.first
//                val startDate = Date(pair.first)
                val formattedStartDate = simpleDateFormat.format(startDate)
//                val endDate = Date(pair.second)
                val endDate = pair.second
                val difference = endDate - startDate
                val daysDiff = TimeUnit.MILLISECONDS.toDays(difference)
                val formattedEndDate = simpleDateFormat.format(endDate)
                val totalCost = daysDiff * (cost?.toInt() ?: 0)

                hireItemViewModel.startDateInfo.value = "Start date: $formattedStartDate"
                hireItemViewModel.endDateInfo.value = "End date: $formattedEndDate"
                hireItemViewModel.costInfo.value = "Total cost: $$totalCost"
            }
        }



        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}