package com.example.edistynytmobiiliohjelmointi2023.temporarytokenauth

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.edistynytmobiiliohjelmointi2023.placeholder.PlaceholderContent.PlaceholderItem
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentTemporaryTokenBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class TemporaryTokenRecyclerViewAdapter(
    private val valuesFeedBack: List<FeedBackForTemporaryToken>
) : RecyclerView.Adapter<TemporaryTokenRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTemporaryTokenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = valuesFeedBack[position]
        //holder.idView.text = item.id
        holder.contentView.text = item.id.toString()+"\n"+
                item.name.toString()+"\n"+
                item.location.toString()+"\n"+
                item.value.toString()+"\n"
    }

    override fun getItemCount(): Int = valuesFeedBack.size

    inner class ViewHolder(binding: FragmentTemporaryTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}