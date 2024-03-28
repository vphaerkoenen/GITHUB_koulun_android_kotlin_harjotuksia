/////////////////////// TAITEEN SÄÄNTÖJEN MUKAAN TÄMÄ TIEDOSTO KUULUISI feedbackpackageen ///////////////////////////
////////////////////// MUTTA ANDROID STUDIO EI PIDÄ SIITÄ JA EI LÖYDÄ TÄTÄ TIEDOSTOA ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.example.edistynytmobiiliohjelmointi2023

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentEditFeedBackBinding
import com.example.edistynytmobiiliohjelmointi2023.feedback.FeedBack
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.UnsupportedEncodingException


/**
 * A simple [Fragment] subclass.
 * Use the [EditFeedBackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFeedBackFragment : Fragment() {
    private var _binding: FragmentEditFeedBackBinding? = null
    private val binding get() = _binding!!
    val args: EditFeedBackFragmentArgs by navArgs()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditFeedBackBinding.inflate(inflater, container, false)
        val root: View = binding.root
        linearLayoutManager = LinearLayoutManager(requireContext().applicationContext)
        // Muista tehdä erillinen lista fragmentin ylätasolle (luokan jäsenmuuttujaksi)
        // jonne voimme tallentaa haetut Feedbackit
        var feedback: List<FeedBack> = emptyList();
        val gson = GsonBuilder().setPrettyPrinting().create()
        // Request a string response from the provided URL.
        Log.d("editfeedbackfragment terveiset", "Aletaan hakemaan dataa...")


        //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        // Osoite täytyy olla ajantasalla kolmessa tiedostossa:¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        // FeedBackFragment, FeedBackAdapter, EditFeedBackFragment¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
        //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val JSON_URL = "https://2137-86-115-117-125.ngrok-free.app/items/feedback/" + args.id + "?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//tämä osoite on ngrok-tunnelointia varten
        //val JSON_URL = "http://127.0.0.1:8055/items/feedback?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"//////////
        //val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=Krfa9kwy4BDWksc2QhWLuJrr6r-CGYKM"///////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Toast.makeText(
            requireContext().applicationContext,
            "Wait,this can take a while...",
            Toast.LENGTH_SHORT
        ).show()


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////// Seuraava lataa yksittäisen feedbackin APIsta siirryttäessä tähän fragmenttiin////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        try {
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->

                    // koska Directus tallentaa varsinaisen datan kenttään "data", pitää meidän
                    // suodattaa alkuperäistä JSONia hieman
                    var id = JSONObject(response).getJSONObject("data").get("id")
                    var name = JSONObject(response).getJSONObject("data").get("name")
                    var location = JSONObject(response).getJSONObject("data").get("location")
                    var value = JSONObject(response).getJSONObject("data").get("value")
                    Log.d("apidata", name.toString())
                    Log.d("apidata", location.toString())
                    Log.d("apidata", value.toString())
                    binding.editFeedBackId.setText(id.toString())
                    binding.editFeedBackName.setText(name.toString())
                    binding.editFeedBackLocation.setText(location.toString())
                    binding.editFeedBackValue.setText(value.toString())


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
        } catch (e: Exception) {
            Toast.makeText(
                requireContext().applicationContext,
                "Problems with single feedback data",
                Toast.LENGTH_LONG
            ).show()
        }


//¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
//¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ Seuraava yrittää päivittää yksittäistä feedbackia painettaessa nappia ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
//¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

        binding.buttonUpdateSingleFeedback.setOnClickListener() {
            try {
                var id = binding.editFeedBackId.id
                var name = binding.editFeedBackName.text
                var location = binding.editFeedBackLocation.text
                var value = binding.editFeedBackValue.text

                // Request a string response from the provided URL.
                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.PATCH, JSON_URL,
                    Response.Listener { response ->

                        binding.editFeedBackId.text?.clear()
                        binding.editFeedBackName.text?.clear()
                        binding.editFeedBackLocation.text?.clear()
                        binding.editFeedBackValue.text?.clear()
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
                            f.id = id
                            f.name = name.toString()
                            f.location = location.toString()
                            f.value = value.toString()

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
                    "Updating single feedback...",
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(
                    context,
                    "Press 'GET FEEDBACK FROM API' on previous page\n to see updated feedbacks",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext().applicationContext,
                    "Problems with updating single feedback data",
                    Toast.LENGTH_LONG
                ).show()
            }

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

}


