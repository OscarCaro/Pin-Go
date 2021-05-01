package com.pinandgo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val FOLDER_BUNDLE_RECYCLER_VIEW = "folder_rec_view"

class FoldersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)

        val adapter = FolderRecyclerViewAdapter(PinList.getMappedList(requireContext()), requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_columns))
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(FOLDER_BUNDLE_RECYCLER_VIEW, recyclerView.layoutManager?.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            recyclerView.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(FOLDER_BUNDLE_RECYCLER_VIEW))
        }
    }

}