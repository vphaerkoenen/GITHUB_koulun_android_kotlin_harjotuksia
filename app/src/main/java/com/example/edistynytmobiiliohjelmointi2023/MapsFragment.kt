package com.example.edistynytmobiiliohjelmointi2023

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(),GoogleMap.OnMarkerClickListener {
    private lateinit var gMap : GoogleMap
    private var _binding: FragmentMapsBinding?=null
    private val binding get()=_binding!!
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        gMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        var m1 = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        m1?.tag="Sydney"

        val taivalkoski = LatLng(65.57533117405069, 28.243676067868552)
        val rovaniemi = LatLng(66.50163035406298, 25.71387601458986)
        var m2 = googleMap.addMarker(MarkerOptions().position(taivalkoski).title("Marker in Taivalkoski"))
        m2?.tag="Taivalkoski"

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taivalkoski,5f))

        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val taivalkoski = LatLng(65.57533117405069, 28.243676067868552)
        val rovaniemi = LatLng(66.50163035406298, 25.71387601458986)

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.checkBoxZoomControl.setOnCheckedChangeListener { compoundButton, b ->
            gMap.uiSettings.isZoomControlsEnabled = b
        }

        binding.radioButtonMapNormal.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        binding.radioButton2MapHybrid.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }

        binding.radioButton3MapTerrain.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }


        binding.buttonDrawRoute.setOnClickListener(){
            gMap.addPolyline((PolylineOptions()).add(taivalkoski,rovaniemi).
                // below line is use to specify the width of poly line.
            width(5F)
                // below line is use to add color to our poly line.
                .color(Color.RED)
                // below line is to make our poly line geodesic.
                .geodesic(true));
            // on below line we will be starting the drawing of polyline.
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taivalkoski, 5F))

        }

        binding.buttonDrawCircle.setOnClickListener(){
            gMap.addCircle(CircleOptions().
            center(taivalkoski).
            radius(10000.0).
            strokeWidth(5F).
            strokeColor(Color.RED))
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taivalkoski,10F))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("Testi", "onmarkerclick toimii")
        Log.d("Testi",p0.position.latitude.toString())
        Log.d("Testi",p0.position.longitude.toString())
        Log.d("Testi",p0.tag.toString())
        val action = MapsFragmentDirections.actionMapsFragmentToCityWeatherFragment(
            p0.position.latitude.toFloat(),
            p0.position.longitude.toFloat())
        findNavController().navigate(action)

        return false
    }

}


