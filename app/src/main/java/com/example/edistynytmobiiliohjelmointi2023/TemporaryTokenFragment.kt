package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentTemporaryTokenBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentTemporaryTokenListBinding
import com.example.edistynytmobiiliohjelmointi2023.feedback.FeedBackAdapter
import com.example.edistynytmobiiliohjelmointi2023.temporarytokenauth.FeedBackForTemporaryToken
import com.example.edistynytmobiiliohjelmointi2023.temporarytokenauth.TemporaryTokenRecyclerViewAdapter
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.UnsupportedEncodingException

/**
 * A fragment representing a list of Items.
 */
class TemporaryTokenFragment : Fragment() {

   // private var _binding : FragmentTemporaryTokenBinding?=null

    private var _binding : FragmentTemporaryTokenListBinding?=null
    private val binding get()=_binding!!



    // VARIABLES USED BY THE SESSION MANAGEMENT
    val LOGIN_URL = "https://2137-86-115-117-125.ngrok-free.app/auth/login"

    // these should be placed in the local properties file and used by BuildConfig
    // JSON_URL should be WITHOUT a trailing slash (/)!
    val JSON_URL = "https://2137-86-115-117-125.ngrok-free.app"

    // only if the services uses this (e.g. IBM Cloud), otherwise, not needed
    val apikey = "YOUR API KEY"

    // if using username + password in the service (e.g. Directus), use these
    val username = "joku@jossain.com"
    val password = "Password123!"

    // request queues for requests
    var requestQueue: RequestQueue? = null
    var refreshRequestQueue: RequestQueue? = null

    // state booleans to determine our session state
    var loggedIn: Boolean = false
    var needsRefresh: Boolean = false

    // stored tokens. refresh is used when our session token has expired
    // access token in this case is the same as session token
    var refreshToken = ""
    var accessToken =  ""

    // Muista tehdä erillinen lista fragmentin ylätasolle (luokan jäsenmuuttujaksi)
    // jonne voimme tallentaa haetut Feedbackit
    var feedbacks: List<FeedBackForTemporaryToken> = emptyList();
    val gson = GsonBuilder().setPrettyPrinting().create()
    // Request a string response from the provided URL.
    // REQUEST OBJECT 3 : ACTUAL DATA -> FEEDBACK
    lateinit var adapter: TemporaryTokenRecyclerViewAdapter

    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTemporaryTokenListBinding.inflate(inflater,container,false)
        val root: View = binding.root



        return root
    }



    // fragment entry point
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(context,
                    "Wait, this takes a while...",
                        Toast.LENGTH_SHORT).show()

        requestQueue = Volley.newRequestQueue(context)
        refreshRequestQueue = Volley.newRequestQueue(context)
        linearLayoutManager = LinearLayoutManager(requireContext().applicationContext)
        // start with login
        loginAction()
    }

    fun dataAction() {
        if (loggedIn) {
            requestQueue?.add(dataRequest)
        }
    }

    fun loginAction()
    {
        Log.d("ADVTECH", "login")
        Log.d("ADVTECH", JSON_URL + " login")
        requestQueue?.add(loginRequest)
    }


    fun refreshLogin() {
        if (needsRefresh) {
            loggedIn = false
            // use this if using refresh logic
            //refreshRequestQueue?.add(loginRefreshRequest)

            // if using refresh logic, comment this line out
            loginAction()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


    // REQUEST OBJECT 1: LOGIN
    var loginRequest: StringRequest = object : StringRequest(
        Request.Method.POST, LOGIN_URL,
        Response.Listener { response ->

            var responseJSON: JSONObject = JSONObject(response)

            // save the refresh token too if using refresh logic
            // refreshToken = responseJSON.get("refresh_token").toString()

            // this part depends completely on the service that is used
            // Directus uses the data -> access_token -approach
            // IBM Cloud handles the version in comments
            // accessToken = responseJSON.get("access_token").toString()
            accessToken = responseJSON.getJSONObject("data").get("access_token").toString()
            Log.d("accesstoken received", accessToken)
            loggedIn = true
            Toast.makeText(context,
                            "Access Token received...",
                                Toast.LENGTH_SHORT).show()

            // after login's done, get data from API
            dataAction()

        },
        Response.ErrorListener {
            // typically this is a connection error
            Log.d("ADVTECH", it.toString()+"\n"+
                    "Problems with signing in to API")
            Toast.makeText(requireContext().applicationContext,
                            it.toString()+"\n"+
                                    "Problems with signing in to API",
                            Toast.LENGTH_LONG).show()
        }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            // we have to provide the basic header info
            // + Bearer info => accessToken
            val headers = HashMap<String, String>()
            headers["Accept"] = "application/json"

            // IBM Cloud expects the Content-Type to be the following:
            // headers["Content-Type"] = "application/x-www-form-urlencoded"

            // for Directus, the typical approach works:
            headers["Content-Type"] = "application/json; charset=utf-8"
            headers["ngrok-skip-browser-warning"] = ""//tämä on ngrok:ia varten

            return headers
        }



        // use this to build the needed JSON-object
        // this approach is used by Directus, IBM Cloud uses the commented version instead
        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            // this function is only needed when sending data
            var body = ByteArray(0)
            try {
                // on how to create this newData -variable
                var newData = ""

                // a very quick 'n dirty approach to creating the needed JSON body for login
                newData = "{\"email\":\"${username}\", \"password\": \"${password}\"}"

                // JSON to bytes
                body = newData.toByteArray(Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                // problems with converting our data into UTF-8 bytes
            }


            return body
        }

    }





    var dataRequest: StringRequest = object : StringRequest(

        Request.Method.GET, JSON_URL+"/items/feedback",
        Response.Listener { response ->
            Log.d("ADVTECH", response);
            val jObject = JSONObject(response)
            val jArray = jObject.getJSONArray("data")
            feedbacks =
                gson.fromJson(jArray.toString(), Array<FeedBackForTemporaryToken>::class.java).toList()
            Log.d("apidata", jArray.toString())
            Log.d("apidata", feedbacks.toString())
            Toast.makeText(context,
                            "Data arrived, preparing to show...",
                                Toast.LENGTH_SHORT).show()
            adapter = TemporaryTokenRecyclerViewAdapter(feedbacks)
            binding.temporaryTokenRecyclerView.layoutManager = linearLayoutManager
            binding.temporaryTokenRecyclerView.adapter = adapter


        },
        Response.ErrorListener {
            // typically this is a connection error
            Log.d("Data visualisation problems", it.toString())
            Toast.makeText(requireContext().applicationContext,"Data visualisation problems"+"\n"+
                        it.toString(),
                        Toast.LENGTH_LONG).show()


            if (it is AuthFailureError) {
                Log.d("Authentication failure", "EXPIRED start")
                Toast.makeText(requireContext().applicationContext,
                                "Kirjautuminen vanhentui, uusi kirjautuminen...",
                                Toast.LENGTH_LONG).show()


                needsRefresh = true
                loggedIn = false
                refreshLogin()

                Log.d("ADVTECH", "EXPIRED end")
            }
        }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            // we have to provide the basic header info
            // + Bearer info => accessToken
            val headers = HashMap<String, String>()
            headers["ngrok-skip-browser-warning"] = ""//tämä on ngrok:ia varten
            // headers["Accept"] = "application/json"
            // headers["Content-Type"] = "application/json; charset=utf-8"
            headers["Authorization"] = "Bearer " + accessToken




            return headers
        }

    }

}

