package com.example.cp3407_assignment.ui.hire_item

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cp3407_assignment.databinding.FragmentHireItemBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
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

        hireItemViewModel.dateInfo.value = getString(
            com.example.cp3407_assignment.R.string.hire_dates_info,
            hire_start_date,
            hire_end_date
        )
        hireItemViewModel.costInfo.value = getString(
            com.example.cp3407_assignment.R.string.hire_cost_info,
            arguments?.getString("cost")
        )

        hireItemViewModel.dateInfo.observe(viewLifecycleOwner) {
            binding.dateInfo.text = hireItemViewModel.dateInfo.value

        }
        hireItemViewModel.timeInfo.observe(viewLifecycleOwner) {
            binding.timeInfo.text = hireItemViewModel.timeInfo.value
        }

        hireItemViewModel.costInfo.observe(viewLifecycleOwner) {
            binding.totalCost.text = hireItemViewModel.costInfo.value
        }

//        if (hire_start_date != null && hire_end_date != null) {
//            setDatePickerDates(hire_start_date, hire_end_date)
//        }


        //val datePicker = binding.datePicker
        // disable dates before start date and after end date
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")

        val start_date = formatter.parse(hire_start_date)
        val end_date = formatter.parse(hire_end_date)

//        datePicker.minDate = start_date.time
//        datePicker.maxDate = end_date.time
//
//
//        datePicker.addOnLayoutChangeListener() { selection ->
//            isDatePickerUsed = true
//            val startDate = selection.first
//            val endDate = selection.second
//        }

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
            }, hour, minute, false
        )

        binding.availableTimes.setOnClickListener {
            mTimePicker.show()
        }

        binding.availableDates.setOnClickListener {
// Construct the dialog
            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, year, month, dayOfMonth ->
                        val date = Calendar.getInstance()
                        date.set(Calendar.YEAR, year)
                        date.set(Calendar.MONTH, month)
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val simpleDateFormat =
                            SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
                        val formattedDate =
                            simpleDateFormat.format(date.time)
                        binding.dateInfo.text = formattedDate
                    },
                    mcurrentTime.get(Calendar.YEAR),
                    mcurrentTime.get(Calendar.MONTH),
                    mcurrentTime.get(Calendar.DAY_OF_MONTH)
                )
            }
//            val constraints = CalendarConstraints.Builder()
//                .setStart(mcurrentTime.timeInMillis)
//                .setEnd(end_date.time)
//                .setValidator(DateValidatorPointBackward.before(end_date.time))
//                .setValidator(DateValidatorPointForward.from(start_date.time))
//                .build()
//
//            val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
//                .setCalendarConstraints(constraints)
//                .setTitleText("Select date")
//                .setPositiveButtonText("Submit")
//                .build()
//
//            materialDatePicker.show(childFragmentManager, "DatePicker")
//
//            materialDatePicker.addOnPositiveButtonClickListener {pair ->
//                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
//                val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
//                val startDate = Date(pair.first)
//                val formattedStartDate = simpleDateFormat.format(startDate)
//                val endDate = Date(pair.second)
//                val formattedEndDate = simpleDateFormat.format(endDate)
//                binding.availableDates.text = "$formattedStartDate until $formattedEndDate"
//            }
            if (datePickerDialog != null) {
                datePickerDialog.datePicker.minDate = start_date.time
                datePickerDialog.datePicker.maxDate = end_date.time
            }
//             Show the dialog
            datePickerDialog?.show()

        }

        return root
    }

    class RangeDateValidator(val numberOfDays: Int) : DateValidator {
        private var rangePicker: MaterialDatePicker<*>? = null

        constructor(parcel: Parcel) : this(parcel.readInt()) {
        }

        fun setDatePicker(rangePicker: MaterialDatePicker<*>?) {
            this.rangePicker = rangePicker
        }

        override fun describeContents(): Int {
            TODO("Not yet implemented")
        }

        override fun isValid(date: Long): Boolean {
            val selection = rangePicker!!.selection as Pair<Long, Long>?
            if (selection != null) {
                val startDate = selection.first
                if (startDate != null) {
                    val days: Long = (numberOfDays - 1) * TimeUnit.DAYS.toMillis(1)
                    if (date > startDate + days) return false
                    if (date < startDate) return false
                }
            }
            return true
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            //super.writeToParcel(parcel, flags)
            //parcel.writeInt(numberOfDays)
        }

        companion object CREATOR : Parcelable.Creator<RangeDateValidator> {
            override fun createFromParcel(parcel: Parcel): RangeDateValidator {
                return RangeDateValidator(parcel)
            }

            override fun newArray(size: Int): Array<RangeDateValidator?> {
                return arrayOfNulls(size)
            }
        }
    }

    private fun setDatePickerDates(hire_start_date: String, hire_end_date: String) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}