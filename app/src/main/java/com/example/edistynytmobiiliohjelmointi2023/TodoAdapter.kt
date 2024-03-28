package com.example.edistynytmobiiliohjelmointi2023

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.edistynytmobiiliohjelmointi2023.databinding.RecyclerViewItemBinding

// Oma RecyclerViewin adapteri nimeltä CommentAdapter, joka vastaanottaa Listan Comment-objekteja
// samalla kytketään CommentHolder-niminen luokka osaksi tätä adapteria (tulee sijaitsemaan
// CommentAdapter-luokan sisällä sisäisenä luokkana (inner class)
class TodoAdapter(private val todos: List<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    // binding layerin muututjien alustaminen
    private var _binding: RecyclerViewItemBinding? = null
    private val binding get() = _binding!!

    // ViewHolderin onCreate-metodi. käytännössä tässä kytketään binding layer
    // osaksi CommentHolder-luokkaan (adapterin sisäinen luokka)
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        // binding layerina toimii yksitätinen recyclerview_item_row.xml -instanssi
        _binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoHolder(binding)
    }

    // tämä metodi kytkee yksittäisen Comment-objektin yksittäisen CommentHolder-instanssiin
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onBindViewHolder
    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val itemTodo = todos[position]
        holder.bindTodo(itemTodo)

    }

    // Adapterin täytyy pysty tietämään sisältämänsä datan koko tämän metodin avulla
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä getItemCount
    override fun getItemCount(): Int {
        return todos.size
    }

    // CommentHolder, joka määritettiin oman CommentAdapterin perusmäärityksessä (ks. luokan yläosa)
    // Holder-luokka sisältää logiikan, jolla data ja ulkoasu kytketään toisiinsa
    class TodoHolder(v: RecyclerViewItemBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        // tämän kommentin ulkoasu ja varsinainen data
        private var view: RecyclerViewItemBinding = v
        private var todo: Todo? = null

        // mahdollistetaan yksittäisen itemin klikkaaminen tässä luokassa
        init {
            v.root.setOnClickListener(this)
        }

        // metodi, joka kytkee datan yksityiskohdat ulkoasun yksityiskohtiin
        fun bindTodo(todo: Todo )
        {
            this.todo = todo
            view.textView4.text = todo.title
            if (todo.completed==true){
                view.imageView5.setImageResource(R.drawable._imageok)
            }else{
                view.imageView5.setImageResource(R.drawable._imagenotok)
            }

            // aseta loput textViewit haluamallasi tavalla tähän
        }

        // jos itemiä klikataan käyttöliittymässä, ajetaan tämä koodio
        override fun onClick(v: View) {
            Log.d("viewclick testi","Yksittäistä itemiä klikattu")

            val action = TodoFragmentDirections.actionTodoFragmentToTodoDetailFragment(todo?.id as Int)
            v.findNavController().navigate(action)



        // tässä esimerkiksi actionilla ja navControllerin avulla
            // navigoidaan toiseen fragmentiin, jonka tarkoitus on näyttää
            // yksityiskohdat tästä kommentista.

            // klikatun kommentin id:n saat haettua näin:
            // comment.id

            // parametrina tulee lähettää joko:
            // 1. pelkkä kommentin id argumenttina
            // 2. kaikki kommentin datat omina argumentteina
            // 3. kaikki data JSON-muodossa yhtenä argumenttina (GSON)

            // TOISESSA FRAGMENTISSA, voit ottaa navArgs / args-muuttujan
            // avulla lähetetyn id-parametrin, ja muodostaa sen avulla esim. uuden URLin:

            // JSON_URL = "https://jsonplaceholder.typicode.com/comments/" + args.id.toString()
        }
    }
}