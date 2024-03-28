package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDataBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentTodoDetailBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TodoDetailFragment : Fragment() {

    private var _binding: FragmentTodoDetailBinding?=null
    private val binding get()=_binding!!
    val args:TodoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("tododetailfragmenttesti","Todon id: "+args.id.toString())

        GlobalScope.launch {
            // this is the url where we want to get our data from
            val JSON_URL = "https://jsonplaceholder.typicode.com/todos/" + args.id.toString()
            val gson = GsonBuilder().setPrettyPrinting().create()
            // Request a string response from the provided URL.
            Log.d("TESTI", "aletaan hakemaan yksittäistä dataa...")
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->

                    // print the response as a whole
                    // we can use GSON to modify this response into something more usable
                    var rows: Todo = gson.fromJson(response, Todo::class.java)
                    //var rows: List<Todo> = gson.fromJson(response, Array<Todo>::class.java).toList()
                    Log.d("rows","rows haettu muuttujaan,tietojen asettaminen näkymään...")

                    binding.detailsUserId.text=rows.userId.toString()
                    binding.detailsId.text=rows.id.toString()
                    binding.detailsTitle.text=rows.title.toString()
                    binding.detailsCompleted.text=rows.completed.toString()

                },
                Response.ErrorListener {
                    // typically this is a connection error
                    Log.d("TESTI", it.toString())
                }) {
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

        return root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding=null
    }
}
