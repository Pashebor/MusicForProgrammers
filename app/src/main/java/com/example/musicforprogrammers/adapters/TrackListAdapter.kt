package com.example.musicforprogrammers.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import java.text.FieldPosition


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

    fun getView(convertView: View, parent: ViewGroup?): View {
        var row: View = convertView
        val inflater = (context as AppCompatActivity).layoutInflater
        row = inflater.inflate(layoutResourceId, parent, false)

        return row
    }
}