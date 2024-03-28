package com.example.edistynytmobiiliohjelmointi2023.basicauth

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.R
import com.example.edistynytmobiiliohjelmointi2023.feedback.FeedBack
import com.example.edistynytmobiiliohjelmointi2023.placeholder.PlaceholderContent
import com.google.gson.GsonBuilder
import org.json.JSONObject

/**
 * A fragment representing a list of Items.
 */
class BasicAuthFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic_auth_list, container, false)



        // this is the url where we want to get our data
        // Note: if using a local server, use http://10.0.2.2 for localhost. this is a virtual address for Android emulators, since
        // localhost refers to the Android device instead of your computer
        var apingwebdata : List<BasicAuth> = emptyList();
        val gson = GsonBuilder().setPrettyPrinting().create()
        val JSON_URL = "https://apingweb.com/api/auth/users"

        Toast.makeText(
            requireContext().applicationContext,
            "Wait, fetching data can take a while...", Toast.LENGTH_SHORT
        ).show()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->
                val jObject = JSONObject(response)
                val jArray = jObject.getJSONArray ("data")
                apingwebdata = gson.fromJson(jArray.toString() , Array<BasicAuth>::class.java).toList()
                Log.d("apingwebdata",apingwebdata.toString())

                // Set the adapter
                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = BasicAuthRecyclerViewAdapter(apingwebdata)
                    }
                }

                // response from API, you can use this in TextView, for example
                // Check also out the example below

                // Note: if you send data to API instead, this might not be needed
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
                Toast.makeText(
                    requireContext().applicationContext,
                    it.toString(), Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise Apigility will block our queries!
                // define we are after JSON data!
                val headers = HashMap<String, String>()
                // replace with your own API's login info
                val authorizationString = "Basic " + Base64.encodeToString(
                    ("admin" + ":" + "12345").toByteArray(), Base64.DEFAULT
                )
                headers.put("Authorization", authorizationString);
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)





        return view
    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            BasicAuthFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}