package com.example.edistynytmobiiliohjelmointi2023.feedback

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.RecyclerViewItemBinding
import com.google.gson.GsonBuilder


// Oma RecyclerViewin adapteri nimeltä CommentAdapter, joka vastaanottaa Listan Comment-objekteja
// samalla kytketään CommentHolder-niminen luokka osaksi tätä adapteria (tulee sijaitsemaan
// CommentAdapter-luokan sisällä sisäisenä luokkana (inner class)
class FeedBackAdapter(private val feedbacks: List<FeedBack>) :
    RecyclerView.Adapter<FeedBackAdapter.FeedBackHolder>() {

    // binding layerin muututjien alustaminen
    private var _binding: RecyclerViewItemBinding? = null
    private val binding get() = _binding!!
    // ViewHolderin onCreate-metodi. käytännössä tässä kytketään binding layer
    // osaksi CommentHolder-luokkaan (adapterin sisäinen luokka)
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBackHolder {
        // binding layerina toimii yksitätinen recyclerview_item_row.xml -instanssi
        _binding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedBackHolder(binding)
    }

    // tämä metodi kytkee yksittäisen Comment-objektin yksittäisen CommentHolder-instanssiin
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onBindViewHolder
    override fun onBindViewHolder(holder: FeedBackHolder, position: Int) {
        val itemFeedBack = feedbacks[position]
        holder.bindFeedBack(itemFeedBack)

    }

    // Adapterin täytyy pysty tietämään sisältämänsä datan koko tämän metodin avulla
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä getItemCount
    override fun getItemCount(): Int {
        return feedbacks.size
    }

    // CommentHolder, joka määritettiin oman CommentAdapterin perusmäärityksessä (ks. luokan yläosa)
    // Holder-luokka sisältää logiikan, jolla data ja ulkoasu kytketään toisiinsa
    class FeedBackHolder(v: RecyclerViewItemBinding) : RecyclerView.ViewHolder(v.root),
        View.OnClickListener, View.OnLongClickListener {

        // tämän kommentin ulkoasu ja varsinainen data
        private var view: RecyclerViewItemBinding = v
        private var feedBack: FeedBack? = null



        // mahdollistetaan yksittäisen itemin klikkaaminen tässä luokassa
        init {
            v.root.setOnClickListener(this)
            v.root.setOnLongClickListener(this)
        }


        // metodi, joka kytkee datan yksityiskohdat ulkoasun yksityiskohtiin
        fun bindFeedBack(feedBack: FeedBack) {
            this.feedBack = feedBack
            view.textView4.text =
                feedBack.id.toString() + "." + "\n" +
                        "Name: " + feedBack.name.toString() + "\n" +
                        "Location: " + feedBack.location.toString() + "\n" +
                        "Value: " + feedBack.value.toString()

        }


        /////////////jos itemiä klikataan käyttöliittymässä, ajetaan tämä koodio (päästään muokkaamaan feedbackia)
        override fun onClick(v: View) {

            var id = feedBack?.id?.toString()
            Log.d("viewclick testi", "Yksittäistä itemiä klikattu feedbackadapterissa")
            Log.d("viewclick testi", id.toString())
            FragmentManager.findFragment<FeedBackFragment>(v)
                .navigateToEditFeedBack(id.toString(), v)
        }


        /////////////jos itemiä klikataan pitkään käyttöliittymässä, ajetaan tämä koodio (deletoi feedbackin)
        override fun onLongClick(v: View?): Boolean {
            try {
                Toast.makeText(
                    v?.rootView?.context,
                    "Deleting chosen feedback...",
                    Toast.LENGTH_SHORT
                ).show()

                var id = feedBack?.id.toString()

                Log.d("onlongclick testi", "Kohdetta " + id + " klikattu pitkään")

                //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
                // Osoite täytyy olla ajantasalla kolmessa tiedostossa:¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
                // FeedBackFragment, FeedBackAdapter, EditFeedBackFragment¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
                //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

                val JSON_URL = "https://2137-86-115-117-125.ngrok-free.app/items/feedback/" + id + "?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//tämä osoite on ngrok-tunnelointia varten
                //val JSON_URL = "http://10.0.2.2:8055/items/feedback/"+id+"?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"

                val gson = GsonBuilder().setPrettyPrinting().create()
                // Request a string response from the provided URL.

                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.DELETE, JSON_URL,
                    Response.Listener
                    {
                        //Tämä on tyhjää täynnä koska metodi on "delete"
                    },
                    Response.ErrorListener {
                        // typically this is a connection error
                        Log.d("TESTI error", it.toString())
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        // basic headers for the data
                        val headers = HashMap<String, String>()
                        headers["ngrok-skip-browser-warning"] = ""//tämä on ngrok:ia varten
                        headers["Accept"] = "application/json"
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        return headers
                    }
                }

                // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
                // if using this in an activity, use "this" instead of "context"
                val requestQueue =
                    Volley.newRequestQueue(v?.rootView?.context) //newRequestQueue(view?.context )
                requestQueue.add(stringRequest)

                Toast.makeText(
                    v?.rootView?.context,
                    "Press 'GET FEEDBACK FROM API' to see updated feedbacks",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    v?.rootView?.context,
                    "Problems with deleting feedback",
                    Toast.LENGTH_LONG
                ).show()
            }

            return true
        }

    }

}
