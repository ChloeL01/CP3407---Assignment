package com.example.cp3407_assignment.ui.current_hire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.squareup.picasso.Picasso

class CurrentlyHiringItemAdapter(private val mContext: Context, dogs: List<Dog>) :

    RecyclerView.Adapter<CurrentlyHiringItemAdapter.ImageViewHolder>() {
        private val mDogs: List<Dog>
        private var onClickListener: OnClickListener? = null

        init {
            mDogs = dogs
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val v: View = LayoutInflater.from(mContext).inflate(R.layout.hired_out_item, parent, false)
            return ImageViewHolder(v)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val uploadCurrent: Dog = mDogs[position]
            holder.textViewName.text = uploadCurrent.doggo_name
            holder.textViewStartDate.text = uploadCurrent.hire_start_date
            holder.textViewEndDate.text = uploadCurrent.hire_end_date
            //holder.textViewCost.text = "$" + uploadCurrent.cost

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, uploadCurrent)
                }
            }

            Picasso.with(mContext)
                .load(uploadCurrent.imageUrl)
                .fit()
                .centerCrop()
                .into(holder.imageView)
        }

        override fun getItemCount(): Int {
            return mDogs.size
        }

        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }

        // onClickListener Interface
        interface OnClickListener {
            fun onClick(position: Int, model: Dog)
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textViewName: TextView
            var textViewStartDate: TextView
            var textViewEndDate: TextView
            //var textViewCost: TextView
            var imageView: ImageView

            init {
                textViewName = itemView.findViewById(R.id.pet_name)
                textViewStartDate = itemView.findViewById(R.id.pet_start_date)
                textViewEndDate = itemView.findViewById(R.id.pet_end_date)
                //textViewCost = itemView.findViewById(R.id.text_view_cost)
                imageView = itemView.findViewById(R.id.pet_image)
            }
        }
    }








