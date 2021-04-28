package com.pinandgo

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerViewAdapter(private val listSize : Int) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view){
        val textTitle : TextView = view.findViewById(R.id.textTitle)
        val textDesc : TextView = view.findViewById(R.id.textDesc)
        val textDomain : TextView = view.findViewById(R.id.textDomain)
        val imageView : ImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pin = PinList.get(position, holder.itemView.context)
        holder.textTitle.text = pin.title
        holder.textDesc.text = pin.description
        holder.textDomain.text = pin.domain

        Glide.with(holder.itemView.context)
            .load(pin.imageUrl)
            .fitCenter()
            .into(holder.imageView)
        //TODO: loading wheel placeholder and NoIntenet placeholder

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pin.intentUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listSize
    }

}