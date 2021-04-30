package com.pinandgo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListFragment : Fragment() {

    companion object {
        fun newInstance(args: Bundle) = ListFragment().apply {
            arguments = args
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        searchEditText = view.findViewById(R.id.search_edit_text)

        val adapter = RecyclerViewAdapter(PinList.getList(requireContext()), requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchEditText.doOnTextChanged{ charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
            adapter.filter.filter(charSequence)
        }
    }

}