package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentCityWeatherBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentOSMWeatherBinding
import com.example.edistynytmobiiliohjelmointi2023.datatypes.CityWeather
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OSMWeatherFragment : Fragment() {
    private var _binding : FragmentOSMWeatherBinding ?=null
    private val binding get()=_binding!!
    val args:OSMWeatherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOSMWeatherBinding.inflate(inflater,container,false)
        val root: View = binding.root

        getWeatherData()

        binding.buttonCloseWeatherdetails.setOnClickListener(){
            findNavController().navigate(R.id.action_OSMWeatherFragment_to_openStreetMapFragment)
        }

        return root
    }

    fun getWeatherData(){
        GlobalScope.launch {
            // this is the url where we want to get our data from
            val API_KEY=BuildConfig.OPENWEATHER_API_KEY
            val JSON_URL =
                "https://api.openweathermap.org/data/2.5/weather?lat=${args.latitude}&&units=metric&lon=${args.longitude}&appid=${API_KEY}"
            val gson = GsonBuilder().setPrettyPrinting().create()


            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->
                    var item : CityWeather = gson.fromJson(response, CityWeather::class.java)
                    // print the response as a whole
                    // we can use GSON to modify this response into something more usable
                    Log.d("testi", response)

                    val temperature = item.main?.temp
                    val humidity = item.main?.humidity
                    val wind_speed = item.wind?.speed

                    Log.d("testi",temperature.toString())
                    Log.d("testi",humidity.toString())
                    Log.d("testi",wind_speed.toString())

                    binding.layoutTemperature.text=item.main?.temp.toString()+" degrees"
                    binding.layoutHumidity.text=item.main?.humidity.toString()+"%"
                    binding.layoutWindspeed.text=item.wind?.speed.toString()+" m/s"


                },
                Response.ErrorListener {
                    // typically this is a connection error
                    Log.d("testi", it.toString())
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}