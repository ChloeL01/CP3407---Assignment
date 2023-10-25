package com.example.cp3407_assignment.ui.hire_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
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
    private var totalCost:  Long = 0

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

        val hire_start_date = requireArguments().getString("start_date", "")
        val hire_end_date = requireArguments().getString("end_date", "")
        val cost = requireArguments().getString("cost", "")

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
        val mcurrentTime = Calendar.getInstance()
        mcurrentTime.time = start_date

        binding.availableTimes.setOnClickListener {
            val hour = binding.timePicker.hour
            val min = binding.timePicker.minute
            hireItemViewModel.timeInfo.value = "Pick up $hour : $min"
        }

        binding.availableDates.setOnClickListener {
            val constraints = calendarConstraints(start_date, end_date)
            val materialDatePicker = materialDatePicker1(constraints)

            materialDatePicker.show(childFragmentManager, "DatePicker")
            materialDatePicker.addOnPositiveButtonClickListener {pair ->
                materialDatePicker(pair, cost)
            }
        }

        binding.completePayment.setOnClickListener {
            if (checkAllEditTextsNotEmpty()) {
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
                        "owner_contact" to arguments?.getString("owner_contact")
                    )

//                findNavController().navigate(
//                    R.id.action_hireItem_to_navigation_cart,
//                    bundle
//                )
            }
            }

        return root
    }

    private fun materialDatePicker1(constraints: CalendarConstraints): MaterialDatePicker<Pair<Long, Long>> {
        val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraints)
            .setTitleText("Select date")
            .setPositiveButtonText("Submit")
            .build()
        return materialDatePicker
    }

    private fun calendarConstraints(
        start_date: Date,
        end_date: Date
    ): CalendarConstraints {
        return CalendarConstraints.Builder()
            .setStart(start_date.time)
            .setEnd(end_date.time)
            .setValidator(DateValidatorPointBackward.before(end_date.time))
            .setValidator(DateValidatorPointForward.from(start_date.time))
            .build()
    }

    private fun materialDatePicker(pair: Pair<Long, Long>, cost: String?) {
        //                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        val startDate = pair.first
//                val startDate = Date(pair.first)
        val formattedStartDate = simpleDateFormat.format(startDate)
//                val endDate = Date(pair.second)
        val endDate = pair.second
        val difference = endDate - startDate
        val daysDiff = TimeUnit.MILLISECONDS.toDays(difference)
        val formattedEndDate = simpleDateFormat.format(endDate)
        totalCost = daysDiff * (cost?.toInt() ?: 0)

        hireItemViewModel.startDateInfo.value = "Start date: $formattedStartDate"
        hireItemViewModel.endDateInfo.value = "End date: $formattedEndDate"
        hireItemViewModel.costInfo.value = "Total cost: $$totalCost"
    }

    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.endDateInfo.text.toString().isNotEmpty() && binding.startDateInfo.text.toString()
            .isNotEmpty() &&
                binding.timeInfo.text.toString().isNotEmpty() && binding.totalCost.text.toString()
            .isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}