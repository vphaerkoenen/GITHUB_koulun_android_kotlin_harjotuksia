package com.example.edistynytmobiiliohjelmointi2023

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentCommentDataBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentTodoBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.RecyclerViewItemBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.UnsupportedEncodingException

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private var _binding2:RecyclerViewItemBinding?=null
    private val binding2 get() = _binding2!!

    private lateinit var adapter: TodoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        linearLayoutManager = LinearLayoutManager(requireContext().applicationContext)

        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding2 = RecyclerViewItemBinding.inflate(inflater,container,false)
        val root2:View=binding2.root

        binding.buttonGetTodos.setOnClickListener {
            getTodos(requireContext().applicationContext)
        }

        binding.buttonSendTodos.setOnClickListener {
            sendTodos(requireContext().applicationContext)
        }

        return root
    }

    fun getTodos(context: Context) {
        GlobalScope.launch {
            Log.d("TESTI", "getTodos() kutsuttu")
            // this is the url where we want to get our data from
            val JSON_URL = "https://jsonplaceholder.typicode.com/todos"
            val gson = GsonBuilder().setPrettyPrinting().create()
            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->

                    // print the response as a whole
                    // we can use GSON to modify this response into something more usable

                   var rows: List<Todo> = gson.fromJson(response, Array<Todo>::class.java).toList()
                    for (item:Todo in rows){
                        Log.d("titles",item.title.toString())

                    }
                    adapter = TodoAdapter(rows)
                    binding.recyclerView.layoutManager=linearLayoutManager
                    binding.recyclerView.adapter = adapter

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
    }


    fun sendTodos(context: Context) {
        GlobalScope.launch {
            val JSON_URL = "https://jsonplaceholder.typicode.com/todos"
            var i: Todo = Todo()
            i.userId = 1
            i.id = 2
            i.title = "test data"
            i.completed = true
            // if using Json2Kotlin, you will most likely have to do it this way instead:
            // val i : Message = Feedback(name = "John Doe", value = "Some message.")

            var gson = GsonBuilder().create();
            var sendableData = gson.toJson(i);
            //var newData = ""

            // create a new TodoItem object here, and convert it to string format (GSON)

            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, JSON_URL,
                Response.Listener { response ->
                    // usually APIs return the added new data back
                    // when sending new data
                    // therefore the response here should contain the JSON version
                    // of the data you just sent below
                    Toast.makeText(context.applicationContext, "Data lisätty", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("ADVTECH", "Lisätty seuraava data: " + response)

                },
                Response.ErrorListener {
                    // typically this is a connection error
                    Log.d("ADVTECH", it.toString())
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    // we have to specify a proper header, otherwise the API might block our queries!
                    // define that we are after JSON data!
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json; charset=utf-8"
                    return headers
                }

                // let's build the new data here
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    // this function is only needed when sending data
                    var body = ByteArray(0)
                    try { // check the example "Converting a Kotlin object to JSON"
                        // on how to create this newData -variable
                        // Note: This is just a hardcoded example. You can replace the values by getting inputs from EditTexts for example!

                        // JSON to bytes
                        body = sendableData.toByteArray(Charsets.UTF_8)
                    } catch (e: UnsupportedEncodingException) {
                        // problems with converting our data into UTF-8 bytes
                    }
                    return body
                }
            }

            // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
            // if using this in an activity, use "this" instead of "context"
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(stringRequest)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}