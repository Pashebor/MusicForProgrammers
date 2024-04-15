package com.example.musicforprogrammers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.views.MFPTextView
class TrackListAdapter(
    private val context: Context,
    items: List<String>,
) :
    ArrayAdapter<String>(context, R.layout.list_item, items) {
    private var items: List<String>
    private var selectedPos: Int? = null

    init {
        this.items = items
    }

    private class ViewHolder {
        var textView: MFPTextView? = null
    }

    fun setSelectedPosition(position: Int) {
        selectedPos = position
        this.notifyDataSetChanged()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val customView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false)
        val mfpTextView: MFPTextView = customView.findViewById(R.id.list_text_item)

        val viewHolder = ViewHolder()

        mfpTextView.text = getItem(position)
        viewHolder.textView = mfpTextView
        customView.tag = viewHolder

        if (position == this.selectedPos) {
            mfpTextView.background = context.getDrawable(R.color.green)
            mfpTextView.setTextColor(context.getColor(R.color.black))
        }

        return customView
    }


}