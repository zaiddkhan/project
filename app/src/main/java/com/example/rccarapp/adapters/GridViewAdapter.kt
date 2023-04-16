package com.example.rccarapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.rccarapp.R
import com.example.rccarapp.models.Image
import com.squareup.picasso.Picasso

class GridViewAdapter(context: Context, list: List<Image>) : ArrayAdapter<Image>(context,0,list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listitemView = convertView
        if (listitemView == null) {
            listitemView = LayoutInflater.from(context).inflate(
                R.layout.grid_view_item,
                parent,
                false
            )
        }

        val image = getItem(position)

        val imageView = listitemView!!.findViewById<ImageView>(R.id.imageBox)

        Picasso.get().load(image!!.imageUrl).into(imageView)



        return listitemView
    }
}