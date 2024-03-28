package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentOpenStreetMapBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.config.Configuration.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class OpenStreetMapFragment : Fragment() {
    private var _binding : FragmentOpenStreetMapBinding?=null
    private val binding get()=_binding!!
//    val args : OpenStreetMapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenStreetMapBinding.inflate(inflater,container,false)
        val root: View = binding.root

        getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        binding.map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = binding.map.controller
        mapController.setZoom(7.0)
        val startPoint = GeoPoint(65.57533117405069, 28.243676067868552);
        mapController.setCenter(startPoint);


        //Create a marker like this:

        val firstMarker = Marker(binding.map)
        firstMarker.position=startPoint
        firstMarker.title="Taivalkoski"
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        binding.map.overlays.add(firstMarker)
        binding.map.invalidate()

        firstMarker.setOnMarkerClickListener { marker, mapView ->
            Log.d("testi","markeria klikattu")
            val action = OpenStreetMapFragmentDirections.actionOpenStreetMapFragmentToOSMWeatherFragment(
                65.57533117405069f,
                28.243676067868552f)
            findNavController().navigate(action)
            return@setOnMarkerClickListener false
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}