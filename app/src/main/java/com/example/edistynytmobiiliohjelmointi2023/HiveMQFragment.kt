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
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentHiveMQBinding
import com.example.edistynytmobiiliohjelmointi2023.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.util.*


class HiveMQFragment : Fragment() {
    private var _binding : FragmentHiveMQBinding?=null
    private val binding get()=_binding!!
  //  val args:DetailFragmentArgs by navArgs()
    private lateinit var client: Mqtt3AsyncClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHiveMQBinding.inflate(inflater,container,false)
        val root: View = binding.root



// version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier("android2023testi123")//(BuildConfig.MQTT_CLIENT_ID+ UUID.randomUUID().toString())
            .serverHost(BuildConfig.HIVEMQ_BROKER)//MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()



        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.HIVEMQ_USERNAME)//MQTT_USERNAME)
            .password(BuildConfig.HIVEMQ_PASSWORD.toByteArray())//MQTT_PASSWORD.toByteArray())
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


        binding.hivemqButton.setOnClickListener() {
            var stringPayload = binding.hivemqEdittext.text.toString()
            client.publishWith()
                .topic(BuildConfig.HIVEMQ_TOPIC)
                .payload(stringPayload.toByteArray())
                .send()
            binding.hivemqEdittext.text.clear()
            Toast.makeText(context,
                            "Message sended!",
                                Toast.LENGTH_SHORT).show()
            closeKeyboard(binding.root)

        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        client.disconnect()
    }

    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun subscribeToTopic()
    {
        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.HIVEMQ_TOPIC )//MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                activity?.runOnUiThread{
                    binding.hivemqTextview.text = result
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
