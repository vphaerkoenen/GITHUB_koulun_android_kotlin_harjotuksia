package com.example.edistynytmobiiliohjelmointi2023.basicauth

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.edistynytmobiiliohjelmointi2023.placeholder.PlaceholderContent.PlaceholderItem
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentBasicAuthBinding
import com.google.gson.annotations.SerializedName

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class BasicAuthRecyclerViewAdapter(private val values: List<BasicAuth>) : RecyclerView.Adapter<BasicAuthRecyclerViewAdapter.BasicAuthHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicAuthHolder {

        return BasicAuthHolder(
            FragmentBasicAuthBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: BasicAuthHolder, position: Int) {
        val item = values[position]
//        holder.idView.text = item.id
//        holder.contentView.text = item.content
        holder.contentView.text =
            "Id: "+item.userId+"\n"+
            "Name: "+item.name+"\n"+
            "Age: "+item.age+"\n"+
            "Email: "+item.email+"\n"+
            "Data created: "+item.dateCreated
    }


    override fun getItemCount(): Int = values.size

    inner class BasicAuthHolder(binding: FragmentBasicAuthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content



        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }


    }

}