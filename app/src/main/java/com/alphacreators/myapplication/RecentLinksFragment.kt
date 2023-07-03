package com.alphacreators.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView


class RecentLinksFragment : Fragment() {

   private lateinit var listView : ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recent_links, container, false)
        listView = view.findViewById(R.id.recentLinkList)

        // reterive data from parcelable Arraylist and set to listview

        val recentLinks = arguments?.getParcelableArrayList<RecentLinks>("recentLinksList")
        val adapter = recentLinks?.toList()?.let { RecentLinksAdapter(requireContext(), it) }
        listView.adapter = adapter
        return view
    }


}