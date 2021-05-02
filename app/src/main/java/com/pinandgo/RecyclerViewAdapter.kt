package com.pinandgo

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
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
        val favSymbolImage : ImageView = view.findViewById(R.id.symbol_fav)
        val imageView : ImageView = view.findViewById(R.id.image)
        val openButton : ImageButton = view.findViewById(R.id.button_open)
        val deleteButton : ImageButton = view.findViewById(R.id.button_delete)
        val favoriteButton : ImageButton = view.findViewById(R.id.button_favorite)
        val shareButton : ImageButton = view.findViewById(R.id.button_share)
        val buttonGroup: ConstraintLayout = view.findViewById(R.id.button_group)
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
        holder.textDate.text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(pin.date).toString()

        Glide.with(holder.itemView.context)
            .load(pin.imageUrl)
            .circleCrop()
            .into(holder.imageView)
        //TODO: loading wheel placeholder and NoIntenet placeholder

        if (pin.fav){
            holder.favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
            holder.favSymbolImage.visibility = View.VISIBLE
        }
        else{
            holder.favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
            holder.favSymbolImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pin.intentUrl)
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            if (holder.buttonGroup.visibility == View.VISIBLE){
                holder.buttonGroup.visibility = View.GONE
            }
            else{
                holder.buttonGroup.visibility = View.VISIBLE
            }
            true
        }

        holder.openButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pin.intentUrl)
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener{
            PinList.delete(pin, holder.itemView.context)
            notifyItemRemoved(position)
        }

        holder.favoriteButton.setOnClickListener{
            if (pin.fav){
                PinList.changeFav(false, pin, holder.itemView.context)
                holder.favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.favSymbolImage.visibility = View.VISIBLE
            }
            else{
                PinList.changeFav(true, pin,  holder.itemView.context)
                holder.favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
                holder.favSymbolImage.visibility = View.GONE
            }
            notifyItemChanged(position)
        }

        holder.shareButton.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, pin.intentUrl)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            holder.itemView.context.startActivity(shareIntent)
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