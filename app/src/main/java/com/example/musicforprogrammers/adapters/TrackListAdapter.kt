package com.example.musicforprogrammers.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class TrackListAdapter(
    private val context: Context,
    private val layoutResourceId: Int,
    items: List<String>
) :
    ArrayAdapter<String?>(context, layoutResourceId, items) {
    private val items: List<String>

    init {
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }
}