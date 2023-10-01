package com.example.cp3407_assignment.ui.hireschedule

import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
    lateinit var dayOfMonth: TextView
    lateinit var onItemListener : OnItemClickListener

    //TODO implement binding here for dayOfMonth
    override fun onClick(view: View?) {
//    onItemListener.onItemClick(adapterPosition, dayOfMonth.text)
    }






}
