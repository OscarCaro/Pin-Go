package com.pinandgo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
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

    // Input Group
    private lateinit var inputGroup : ConstraintLayout
    private lateinit var inputButton : Button
    private lateinit var inputEditText : EditText

    // Progress Bar:
    private lateinit var progressBar: ProgressBar

    // Result group:
    private lateinit var resultGroup : ConstraintLayout
    private lateinit var textTitle : TextView
    private lateinit var textDesc : TextView
    private lateinit var textDomain : TextView
    private lateinit var textDate : TextView
    private lateinit var imageView : ImageView
    private lateinit var openButton : ImageButton
    private lateinit var deleteButton : ImageButton
    private lateinit var favoriteButton : ImageButton
    private lateinit var shareButton : ImageButton
    private lateinit var buttonGroup: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Result Group
        resultGroup = view.findViewById(R.id.result_group)
        textTitle = view.findViewById(R.id.textTitle)
        textDesc = view.findViewById(R.id.textDesc)
        textDomain = view.findViewById(R.id.textDomain)
        textDate= view.findViewById(R.id.textDate)
        imageView = view.findViewById(R.id.image)
        openButton = view.findViewById(R.id.button_open)
        deleteButton = view.findViewById(R.id.button_delete)
        favoriteButton = view.findViewById(R.id.button_favorite)
        shareButton = view.findViewById(R.id.button_share)
        buttonGroup = view.findViewById(R.id.button_group)

        // Progress Bar
        progressBar = view.findViewById(R.id.progress_bar)

        // InputGroup
        inputGroup = view.findViewById(R.id.inputGroup)
        inputButton = view.findViewById(R.id.inputButton)
        inputEditText= view.findViewById(R.id.inputEditText)
        inputButton.setOnClickListener{
            if (inputEditText.isVisible && inputEditText.text.toString() != ""){
                handleIntent(inputEditText.text.toString())
            }
        }

        //////////////////////////////////////////////////////////////////////////////////

        val intentLink : String? = arguments?.getString(BUNDLE_INTENT_LINK)
        if (intentLink != null){                 // Process incoming intent
            handleIntent(intentLink)
        }
//        else if (savedInstanceState != null){    // Handle device rotation -> repopulate with previous data
//
//        }
        else{                                    // User clicked on navigation bar -> show inputText
            inputGroup.visibility = View.VISIBLE
            resultGroup.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //TODO: save state if pin loaded -> to recover on onCreate
    }

    private fun handleIntent(link : String){
        GlobalScope.launch(Dispatchers.Main) {
            try{
                inputGroup.visibility = View.GONE
                resultGroup.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

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
                    resultGroup.visibility = View.GONE
                    inputGroup.visibility = View.VISIBLE
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
                resultGroup.visibility = View.VISIBLE

            } catch (e: Exception){
                Toast.makeText(requireContext(), "Error loading data from intent", Toast.LENGTH_LONG).show()
                inputGroup.visibility = View.VISIBLE
                resultGroup.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }
}