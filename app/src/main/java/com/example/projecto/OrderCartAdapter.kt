package com.example.projecto

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderCartAdapter(val cartList:MutableList<CartObj>,val context: Context) : RecyclerView.Adapter<OrderCartAdapter.OrderViewHolder>() {
     class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val status: TextView?
        val info:TextView?
        init {
            status = view.findViewById(R.id.paymentStatus)
            info = view.findViewById(R.id.info)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_cart_list,parent,false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.apply {
            this.status?.text = cartList[position].paymentStatus
            val getDogName = cartList[position].beneficiary[0]
            val ownerName = cartList[position].beneficiary[1]
            val date = cartList[position].sponsorDate
            this.info?.text = context.resources.getString(R.string.cart_Info,getDogName,ownerName,date)
        }
    }

}