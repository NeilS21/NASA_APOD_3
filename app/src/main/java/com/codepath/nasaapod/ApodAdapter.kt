package com.codepath.nasaapod

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ApodAdapter(private val apodList: List<ApodItem>) : RecyclerView.Adapter<ApodAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleView: TextView = view.findViewById(R.id.textView)
        val explanationView: TextView = view.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.apod_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = apodList[position]
        holder.titleView.text = "${item.title} (${item.date})"
        holder.explanationView.text = item.explanation

        Glide.with(holder.imageView.context)
                .load(item.url)
                .into(holder.imageView)

    }

    override fun getItemCount() = apodList.size
}