
package com.example.edistynytmobiiliohjelmointi2023.feedback


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentFeedBackBinding
import com.example.edistynytmobiiliohjelmointi2023.EditFeedBackFragment
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class FeedBackFragment : Fragment() {
    private var _binding: FragmentFeedBackBinding? = null
    private val binding get() = _binding!!

    //val args: FeedBackAPIFragmentArgs by navArgs()
    private lateinit var adapter: FeedBackAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBackBinding.inflate(inflater, container, false)
        val root: View = binding.root
        linearLayoutManager = LinearLayoutManager(requireContext().applicationContext)


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Osoitteen kanssa koin vaikeuksia sillä en ole käyttänyt emulaattoria koska tietokoneeni on niin tehoton.¤¤¤¤
        //Emulaattorin sijaan olen käyttänyt android-puhelintani ja en onnistunut saamaan yhteyttä APIin¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        //ennen kuin löysin ngrok-tunnelointiohjelman¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        // Osoite täytyy olla ajantasalla neljässä tiedostossa:¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        // FeedBackFragment, FeedBackAdapter, EditFeedBackFragment, TemporaryTokenFragment ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

        //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        val JSON_URL = "https://2137-86-115-117-125.ngrok-free.app/items/feedback?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//tämä osoite on ngrok-tunnelointia varten
        //val JSON_URL = "http://127.0.0.1:8055/items/feedback?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//¤¤¤¤¤¤¤¤
        //val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//¤¤¤¤¤¤¤¤¤
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        fun getAllFeedBacks() {
            Toast.makeText(
                requireContext().applicationContext,
                "Wait,this can take a while...",
                Toast.LENGTH_SHORT
            ).show()
            Toast.makeText(
                requireContext().applicationContext,
                "You can edit single feedback using short click",
                Toast.LENGTH_SHORT
            ).show()
            Toast.makeText(
                requireContext().applicationContext,
                "You can delete single feedback using long click.",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("testi", "Aletaan hakemaan dataa...")
            // Muista tehdä erillinen lista fragmentin ylätasolle (luokan jäsenmuuttujaksi)
            // jonne voimme tallentaa haetut Feedbackit
            var feedbacks: List<FeedBack> = emptyList();

            val gson = GsonBuilder().setPrettyPrinting().create()
            // Request a string response from the provided URL.

            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->

                    // koska Directus tallentaa varsinaisen datan kenttään "data", pitää meidän
                    // suodattaa alkuperäistä JSONia hieman
                    val jObject = JSONObject(response)
                    val jArray = jObject.getJSONArray("data")
                    feedbacks =
                        gson.fromJson(jArray.toString(), Array<FeedBack>::class.java).toList()
                    Log.d("apidata", jArray.toString())
                    adapter = FeedBackAdapter(feedbacks)
                    binding.recyclerViewFeeBack.layoutManager = linearLayoutManager
                    binding.recyclerViewFeeBack.adapter = adapter

                },
                Response.ErrorListener {
                    // typically this is a connection error
                    Log.d("TESTI error", it.toString())
                    Toast.makeText(
                        requireContext().applicationContext,
                        it.toString() + " Try again after a while", Toast.LENGTH_SHORT
                    ).show()
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
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(stringRequest)
        }


        binding.buttonGetFeedback.setOnClickListener() {
            getAllFeedBacks()
        }


        binding.buttonPostFeedback.setOnClickListener() {

            val name = binding.postApiName.text.toString()
            val location = binding.postApiLocation.text.toString()
            val value = binding.postApiValue.text.toString()
            Log.d("post test", "${name}  ${location}  ${value}")

            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, JSON_URL,
                Response.Listener { response ->

                    binding.postApiName.text?.clear()
                    binding.postApiLocation.text?.clear()
                    binding.postApiValue.text?.clear()
                    closeKeyboard(binding.root)

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
                    headers["ngrok-skip-browser-warning"] = ""//tämä on ngrok:ia varten
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json; charset=utf-8"
                    return headers
                }

                // let's build the new data here
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    // this function is only needed when sending data
                    var body = ByteArray(0)
                    try {
                        //Kotlin-datan muuntaminen JSONiksi:
                        // Note: This is just a hardcoded example. You can replace the values by getting inputs from EditTexts for example!

                        var f: FeedBack = FeedBack();
                        f.name = name
                        f.location = location
                        f.value = value

                        // muutetaan Feedback-olio -> JSONiksi
                        var gson = GsonBuilder().create();
                        var newData = gson.toJson(f);
                        // create a new TodoItem object here, and convert it to string format (GSON)
                        // JSON to bytes
                        body = newData.toByteArray(Charsets.UTF_8)
                    } catch (e: UnsupportedEncodingException) {
                        // problems with converting our data into UTF-8 bytes
                        Toast.makeText(
                            requireContext().applicationContext,
                            "problems with converting our data into UTF-8 bytes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return body
                }
            }

            // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(stringRequest)
            Toast.makeText(
                requireContext().applicationContext,
                "Thank you for your feedback!",
                Toast.LENGTH_SHORT
            ).show()
            Toast.makeText(
                context,
                "Press 'GET FEEDBACK FROM API' to see updated feedbacks",
                Toast.LENGTH_SHORT
            ).show()

        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun navigateToEditFeedBack(id: String, v: View) {

         Log.d("feedbackfragment terveiset", "feedback klikkaus,nyt feedback fragmentissa")

         findNavController().navigate(
            FeedBackFragmentDirections.actionFeedBackFragmentToEditFeedBackFragment(
                id
            )
        )


    }

}
