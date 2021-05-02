package com.pinandgo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val FOLDER_BUNDLE_RECYCLER_VIEW = "folder_rec_view"
const val FOLDER_BUNDLE_NAME = "folder_name"

class FoldersFragment : Fragment() {

    private lateinit var foldersRecyclerView: RecyclerView

    private lateinit var detailGroup : ConstraintLayout
    private lateinit var insideRecyclerView: RecyclerView
    private lateinit var buttonBack : ImageButton
    private lateinit var title: TextView

    private var selectedFolder = ""

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedFolder = savedInstanceState?.getString(FOLDER_BUNDLE_NAME) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foldersRecyclerView = view.findViewById(R.id.folders_recyclerView)

        detailGroup = view.findViewById(R.id.detail_group)
        insideRecyclerView = view.findViewById(R.id.insideRecyclerView)
        buttonBack = view.findViewById(R.id.button_back)
        title = view.findViewById(R.id.text_title_folder)

        val foldersAdapter = FolderRecyclerViewAdapter(PinList.getMappedList(requireContext()), this)
        foldersRecyclerView.adapter = foldersAdapter
        foldersRecyclerView.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_columns))

        if(selectedFolder != ""){
            onFolderClicked(selectedFolder)
        }
    }

    fun onFolderClicked(folderName: String){
        foldersRecyclerView.visibility = View.GONE

        title.text = folderName

        val insideAdapter = RecyclerViewAdapter(PinList.getMappedList(requireContext()).get(folderName)!!, requireContext())
        insideRecyclerView.adapter = insideAdapter
        insideRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        detailGroup.visibility = View.VISIBLE
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(FOLDER_BUNDLE_RECYCLER_VIEW, foldersRecyclerView.layoutManager?.onSaveInstanceState())
        outState.putString(FOLDER_BUNDLE_NAME, title.text.toString())
    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        if(savedInstanceState != null){
//            foldersRecyclerView.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(FOLDER_BUNDLE_RECYCLER_VIEW))
//        }
//    }


}