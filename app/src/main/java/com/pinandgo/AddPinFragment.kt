package com.pinandgo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val BUNDLE_INTENT_LINK = "intent_link"

class AddPinFragment : Fragment() {

    private lateinit var textTitle : TextView
    private lateinit var textDesc : TextView
    private lateinit var textDomain : TextView
    private lateinit var textDate : TextView
    private lateinit var imageView : ImageView
    private lateinit var openButton : ImageButton
    private lateinit var deleteButton : ImageButton
    private lateinit var favoriteButton : ImageButton
    private lateinit var shareButton : ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonGroup: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textTitle = view.findViewById(R.id.textTitle)
        textDesc = view.findViewById(R.id.textDesc)
        textDomain = view.findViewById(R.id.textDomain)
        textDate= view.findViewById(R.id.textDate)
        imageView = view.findViewById(R.id.image)
        openButton = view.findViewById(R.id.button_open)
        deleteButton = view.findViewById(R.id.button_delete)
        favoriteButton = view.findViewById(R.id.button_favorite)
        shareButton = view.findViewById(R.id.button_share)
        progressBar = view.findViewById(R.id.progress_bar)
        buttonGroup = view.findViewById(R.id.button_group)

        val intentLink : String? = arguments?.getString(BUNDLE_INTENT_LINK)

        if (intentLink != null){
            handleIntent(intentLink)
        }
        else{
            //Display editText to enter link and then handle intent
        }
    }

    private fun handleIntent(link : String){
        GlobalScope.launch(Dispatchers.Main) {
            try{
                val pin : Pin = withContext(Dispatchers.IO){
                    Pin(link)           // Throw exception if null -> catch and warn user
                }

                textTitle.text = pin.title.capitalize(Locale.ROOT)
                textDesc.text = pin.description.capitalize(Locale.ROOT)
                textDomain.text = pin.domain
                textDate.text = SimpleDateFormat("HH:mm dd MMMM", Locale.getDefault()).format(pin.date).toString()

                Glide.with(requireContext())
                    .load(pin.imageUrl)
                    .circleCrop()
                    .into(imageView)

                if (pin.fav){
                    favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
                }
                else{
                    favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                }

                PinList.add(pin, requireContext())

                openButton.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(pin.intentUrl)
                    startActivity(intent)
                }

                deleteButton.setOnClickListener{
                    PinList.delete(pin, requireContext())
                    // TODO: exit app
                }

                favoriteButton.setOnClickListener{
                    if (pin.fav){
                        PinList.changeFav(false, pin, requireContext())
                        favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                    }
                    else{
                        PinList.changeFav(true, pin, requireContext())
                        favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
                    }
                }

                shareButton.setOnClickListener{
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, pin.intentUrl)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }

                progressBar.visibility = View.GONE
                buttonGroup.visibility = View.VISIBLE

            } catch (e: Exception){
                Toast.makeText(requireContext(), "Error loading data from intent", Toast.LENGTH_LONG).show()
            }
        }
    }
}