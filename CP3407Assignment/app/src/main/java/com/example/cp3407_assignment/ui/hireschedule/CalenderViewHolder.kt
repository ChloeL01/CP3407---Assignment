package com.example.cp3407_assignment.ui.hireschedule

import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.R

class CalenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
    var dayOfMonth: TextView
    lateinit var onItemListener : OnItemClickListener

    init {
        dayOfMonth = itemView.findViewById(R.id.blockDailyText)
    }


    //TODO implement binding here for dayOfMonth
    override fun onClick(view: View?) {
//    onItemListener.onItemClick(adapterPosition, dayOfMonth.text)
    }






}
