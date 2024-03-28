package com.example.edistynytmobiiliohjelmointi2023

import android.graphics.Color
import android.graphics.drawable.ColorStateListDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentWeatherStationBinding
import com.example.edistynytmobiiliohjelmointi2023.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.text.SimpleDateFormat
import java.util.*


class WeatherStationFragment : Fragment() {
    private var _binding : FragmentWeatherStationBinding?=null
    private val binding get()=_binding!!
    private lateinit var client: Mqtt3AsyncClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherStationBinding.inflate(inflater,container,false)
        val root: View = binding.root

        Toast.makeText(context,
                        "Wait, this takes a while...",
                            Toast.LENGTH_LONG).show()


// version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.MQTT_CLIENT_ID+ UUID.randomUUID().toString())
            .serverHost(BuildConfig.MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()



        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.MQTT_USERNAME)
            .password(BuildConfig.MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {

                    Log.d("ADVTECH", "Connection failure.")

                } else {

                    // Setup subscribes or start publishing

                    subscribeToTopic()

                }
            }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        client.disconnect()
    }


    fun subscribeToTopic()
    {
        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                try {
                    //muutetaan data jsonista weatherstation-olioksi...
                    //Jos JSON-datassa on vain yksi item/objekti:
                    var item: WeatherStation = gson.fromJson(result, WeatherStation::class.java)
                    Log.d("weatherstation data", item.d.get1().v.toString() + " astetta")

                    val temperature = "Temperature: "+item.d.get1().v.toString() + " ℃"
                    val sdf = SimpleDateFormat("HH:mm:ss")
                    val currentDate=sdf.format(Date())
                    var dataText:String="$currentDate - $temperature "

                    activity?.runOnUiThread{
                        binding.letsShowWeatherStationData.text = temperature

                        binding.weatherStationTempInProgressBar.progress = item.d.get1().v.toInt()

                        binding.textviewLatestDataInBox.addData(dataText)

                    }
                }catch (e:Exception){
                    Log.d("diagnostiikkadata","vastaanotettu paketti poikkeaa normaalista")
                }

            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe

                    Log.d("ADVTECH", "Subscribe failed.")

                } else {

                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("ADVTECH", "Subscribed!")

                }
            }

    }
}
