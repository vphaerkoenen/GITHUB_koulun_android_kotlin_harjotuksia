package com.example.edistynytmobiiliohjelmointi2023

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentLocalMQTTBinding
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.util.*


class LocalMQTTFragment : Fragment() {
    private var _binding: FragmentLocalMQTTBinding? = null
    private val binding get() = _binding!!
    private lateinit var client: Mqtt3AsyncClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalMQTTBinding.inflate(inflater, container, false)
        val root: View = binding.root




        binding.buttonConnectToTopic.setOnClickListener() {
            // version 1, onCreateView, public MQTT, no authentication
            client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("4.tcp.eu.ngrok.io")
                .serverPort(17947)
                .buildAsync();



            client.connect()
                .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                    if (throwable != null) {
                        // Handle connection failure
                    } else {
                        // Setup subscribes or start publishing
                        subscribeToTopic()
                    }
                }
        }

        //seuraavat tarvitaan jotta topicin vaihtaminen toimii
        binding.radioButtonDeviceTest.setOnClickListener(){
            client.disconnect()
        }
        binding.radioButtonTestTopic.setOnClickListener(){
            client.disconnect()
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }


    fun subscribeToTopic() {

        var topic: String = ""
        // if (binding.radiogroupChooseTopic.focusedChild==binding.radioButtonTestTopic) {
        if (binding.radioButtonTestTopic.isChecked) {
            topic = "test/topic"
        } else {
            topic = "device/test"
        }

        client.subscribeWith()
            .topicFilter(topic)//(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)
                binding.letsShowLocalmqttMessage.text = result

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
