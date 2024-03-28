package com.example.edistynytmobiiliohjelmointi2023

import android.icu.text.DateFormat.HOUR_MINUTE_SECOND
import android.icu.text.DateFormat.HourCycle
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentChartBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.weatherstation.WeatherStation
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.util.*


class ChartFragment : Fragment() {
    private var _binding : FragmentChartBinding?=null
    private val binding get()=_binding!!
    //val args:DetailFragmentArgs by navArgs()

    // lista lämpötiloja varten
    val temperatureList = mutableListOf<Double>()
    private lateinit var client: Mqtt3AsyncClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartBinding.inflate(inflater,container,false)
        val root: View = binding.root
        var charttypeCounter=0

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



        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("Sääasema")
            .subtitle("Rantavitikan mittauspiste")
            .dataLabelsEnabled(true)
            .yAxisMin(-50)
            .yAxisMax(50)
            .zoomType(AAChartZoomType.Y)
            .series(arrayOf(
                AASeriesElement()
                    .name("Lämpötila")
                    .data(temperatureList.toTypedArray())
              )
            )

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

        //seuraavassa vaihdetaan kuvaajan tyyppiä painamalla nappia
        binding.buttonChangeChartType.setOnClickListener(){
            charttypeCounter++
            if (charttypeCounter==4){
                charttypeCounter=0
            }

            if (charttypeCounter==0){
                binding.aaChartView.aa_refreshChartWithChartModel(aaChartModel.chartType(AAChartType.Line))
            }else if (charttypeCounter==1){
                binding.aaChartView.aa_refreshChartWithChartModel(aaChartModel.chartType(AAChartType.Area))
            }else if (charttypeCounter==2){
                binding.aaChartView.aa_refreshChartWithChartModel(aaChartModel.chartType(AAChartType.Bubble))
            }else{
                binding.aaChartView.aa_refreshChartWithChartModel(aaChartModel.chartType(AAChartType.Column))
            }

            //kuvaajan tyypin vaihtamisessa menee hetki joten
            //annetaan siitä ilmoitus käyttäjälle
            Toast.makeText(context,
                        "Wait!\nLoading new type of chart \n takes a while.",
                            Toast.LENGTH_LONG).show()
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
                    var temperature=item.d.get1().v

                    //lisätään lämpötila listaan
                    temperatureList.add(temperature)

                    //uusi päivitetty aa-elementti päivitetystä listasta
                    var newArray = arrayOf(
                        AASeriesElement()
                            .name("Lämpötila")
                            .data(temperatureList.toTypedArray())
                    )

                    while (temperatureList.size > 10){
                        temperatureList.removeAt(0)
                    }


                    activity?.runOnUiThread{
                        binding.aaChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(newArray,false)


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
