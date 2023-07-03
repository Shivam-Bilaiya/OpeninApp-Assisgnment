package com.alphacreators.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment

class TopLinksFragment : Fragment() {
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top_links, container, false)
        listView = view.findViewById(R.id.listView)


        // reterive data from parcelable Arraylist and set to listview

        val topLinksList = arguments?.getParcelableArrayList<TopLinks>("topLinksList")
        val adapter = topLinksList?.toList()?.let { LinkAdapter(requireContext(), it) }
        listView.adapter = adapter
        return view
    }





}





