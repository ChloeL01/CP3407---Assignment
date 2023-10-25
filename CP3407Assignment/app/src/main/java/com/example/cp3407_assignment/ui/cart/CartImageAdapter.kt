package com.example.cp3407_assignment.ui.cart


import android.content.Context
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.squareup.picasso.Picasso


class CartImageAdapter(private val mContext: Context, dogs: List<Dog>) :
    RecyclerView.Adapter<CartImageAdapter.ImageViewHolder>() {
    private var mDogs: List<Dog>
    private var onClickListener: OnClickListener? = null

    init {
        mDogs = dogs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val lifeCycleOwner = parent.context as LifecycleOwner
        val v: View = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(v, lifeCycleOwner)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uploadCurrent: Dog = mDogs[position]
        holder.textViewName.text = mContext.getString(R.string.hire_doggo_name, uploadCurrent.doggo_name)
        holder.textViewBreed.text = uploadCurrent.doggo_breed
        holder.textViewDate.text = mContext.getString(R.string.hire_date, uploadCurrent.hire_start_date, uploadCurrent.hire_end_date)
        holder.textViewCost.text = mContext.getString(R.string.hire_cost, uploadCurrent.cost)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, uploadCurrent)
            }
        }

        Picasso.with(mContext)
            .load(uploadCurrent.imageUrl)
            .fit()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mDogs.size
    }

    fun searchDataList(searchList: List<Dog>) {
        mDogs = searchList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Dog)
    }

    inner class ImageViewHolder(itemView: View, private val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var textViewBreed: TextView
        var textViewDate: TextView
        var textViewCost: TextView
        var imageView: ImageView

        init {
            textViewName = itemView.findViewById(R.id.text_view_name)
            textViewBreed = itemView.findViewById(R.id.text_view_breed)
            textViewDate = itemView.findViewById(R.id.text_view_start_date)
            textViewCost = itemView.findViewById(R.id.text_view_cost)
            imageView = itemView.findViewById(R.id.image_view_upload)
        }
    }
}