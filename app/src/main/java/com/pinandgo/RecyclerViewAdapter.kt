package com.pinandgo

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val listSize : Int) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view){
        val linkTextView : TextView = view.findViewById(R.id.textLink)
        val domainTextView : TextView = view.findViewById(R.id.textDomain)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pin = PinList.get(position, holder.itemView.context)
        holder.linkTextView.text = pin.link
        holder.domainTextView.text = pin.domain

        holder.linkTextView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pin.link)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listSize
    }

}