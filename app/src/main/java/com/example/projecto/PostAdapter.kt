package com.example.projecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load


class PostAdapter(private val dataSet: MutableList<PostObj>, private val clickListener:OnItemCLickListener) :

    RecyclerView.Adapter<PostAdapter.ViewHolder>(){


     interface OnItemCLickListener{
         fun onItemClick(postObj: PostObj)
     }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dogImg:ImageView
        val dogName:TextView



        init {
            dogImg = view.findViewById(R.id.dogImg)
            dogName = view.findViewById(R.id.dogName)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_list_content, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.apply {
            this.dogName.text = dataSet[position].dogName
            val imgData = dataSet[position].dogImg
            this.dogImg.load(imgData){
                crossfade(true)
                    .placeholder(R.drawable.banner)
            }
            this.itemView.setOnClickListener {
                clickListener.onItemClick(dataSet[position])
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
