package com.alphacreators.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


 //  Adapter for setting listview view data

class LinkAdapter(private val context: Context, var links: List<TopLinks>) : BaseAdapter() {

    override fun getCount(): Int {
        Log.d("eknknd",links.toString())
        Log.d("eofnoenekcne",links.size.toString())
        return links.size
    }

    override fun getItem(position: Int): Any {
        return links[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.top_links_card, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val link = links[position]
        viewHolder.linkTextView.text = link.title
        viewHolder.webLink.text = link.webLink
        viewHolder.totalClicks.text = link.totalClicks.toString()
        viewHolder.date.text = link.createdAt
        loadImageFromInternet(link.originalImage,viewHolder.imageView,context)
        return view
    }

    private fun loadImageFromInternet(originalImage: String?, imageView: ImageView, context: Context) {
          Glide.with(context).load(originalImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
    }

    private class ViewHolder(view: View) {
        val linkTextView: TextView = view.findViewById(R.id.textView3)
        val webLink : TextView = view.findViewById(R.id.textView4)
        val totalClicks : TextView = view.findViewById(R.id.textView2)
        val date : TextView = view.findViewById(R.id.date)
        val imageView : ImageView = view.findViewById(R.id.imageView)
    }
}
