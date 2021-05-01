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

class RecyclerViewAdapter(private var list: ArrayList<Pin>, private val context: Context,)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), Filterable {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textTitle : TextView = view.findViewById(R.id.textTitle)
        val textDesc : TextView = view.findViewById(R.id.textDesc)
        val textDomain : TextView = view.findViewById(R.id.textDomain)
        val textDate : TextView = view.findViewById(R.id.textDate)
        val imageView : ImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pin = list[position]
        holder.textTitle.text = pin.title.capitalize(Locale.ROOT)
        holder.textDesc.text = pin.description.capitalize(Locale.ROOT)
        holder.textDomain.text = pin.domain
        holder.textDate.text = SimpleDateFormat("HH:mm dd MMMM", Locale.getDefault()).format(pin.date).toString()

        Glide.with(holder.itemView.context)
            .load(pin.imageUrl)
            .circleCrop()
            .into(holder.imageView)
        //TODO: loading wheel placeholder and NoIntenet placeholder

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pin.intentUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(query: CharSequence?): FilterResults {
                return FilterResults().apply { this.values = PinList.getSortedList(context, query) }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1 != null){
                    list = p1.values as ArrayList<Pin>
                    notifyDataSetChanged()
                }
            }
        }
    }



}