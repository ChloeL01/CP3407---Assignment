package com.example.cp3407_assignment.ui.home



import com.example.cp3407_assignment.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.squareup.picasso.Picasso



class ImageAdapter(private val mContext: Context, dogs: List<Dog>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val mDogs: List<Dog>

    init {
        mDogs = dogs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v: View = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uploadCurrent: Dog = mDogs[position]
        holder.textViewName.text = uploadCurrent.doggo_name
        Picasso.with(mContext)
            .load(uploadCurrent.imageUrl)
            .fit()
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mDogs.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var imageView: ImageView

        init {
            textViewName = itemView.findViewById(R.id.text_view_name)
            imageView = itemView.findViewById(R.id.image_view_upload)
        }
    }
}