package com.example.cp3407_assignment.ui.hireschedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.R

class CalenderAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalenderViewHolder>() {

    //TODO implement binding here for daysOfMonth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calender_block, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.16666666666666).toInt()
        return CalenderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String)

        @RequiresApi(Build.VERSION_CODES.O)
        fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View
    }
}