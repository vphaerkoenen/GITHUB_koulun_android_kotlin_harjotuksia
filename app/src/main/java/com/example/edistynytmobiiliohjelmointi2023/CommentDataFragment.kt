package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentCommentDataBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDataBinding
import com.google.gson.GsonBuilder

/**
 * A simple [Fragment] subclass.
 * Use the [CommentDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentDataFragment : Fragment() {

    private var _binding: FragmentCommentDataBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCommentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonGetComments.setOnClickListener{
            getComments()
        }

        return root
    }

    fun getComments(){
        Log.d("TESTI","getComments() kutsuttu")
        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/comments"
        val gson = GsonBuilder().setPrettyPrinting().create()
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("TESTI", response)

                var rows : List<Comment> = gson.fromJson(response, Array<Comment>::class.java).toList()
                Log.d("TESTI","Kommenttien määrä on: "+rows.size)

                for (item:Comment in rows){
                    Log.d("TESTI",item.email.toString())
                }

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding=null
    }
}