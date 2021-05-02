package com.pinandgo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FolderRecyclerViewAdapter(private var map: HashMap<String, ArrayList<Pin>>, private val fragment: FoldersFragment)
    : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textTitle : TextView = view.findViewById(R.id.textTitle)
        val imageView : ImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val domain = map.keys.toTypedArray()[position]
        holder.textTitle.text = domain

        Glide.with(holder.itemView.context)
            .load(map.get(domain)?.get(0)?.domainImageUrl)
            .circleCrop()
            .into(holder.imageView)
        //TODO: loading wheel placeholder and NoIntenet placeholder

        holder.itemView.setOnClickListener{
            fragment.onFolderClicked(domain)
        }
    }

    override fun getItemCount(): Int {
        return map.keys.size
    }

}