package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentCustomViewTesterBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding

class CustomViewTesterFragment : Fragment() {
    private var _binding : FragmentCustomViewTesterBinding?=null
    private val binding get()=_binding!!
    //val args:DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomViewTesterBinding.inflate(inflater,container,false)
        val root: View = binding.root


        binding.speedView.speedTo(0f)

        binding.customtemperatureView.changeTemperature(0)

        binding.buttonSetNewTemp.setOnClickListener(){
            var randomTemp:Int=(-50..50).random()
            binding.speedView.speedTo(randomTemp.toFloat())
            binding.customtemperatureView.changeTemperature(randomTemp)
        }

        binding.buttonAddDataToTextview.setOnClickListener(){
            var randomData:Int=(1..100).random()
            binding.textviewLatestDataInBox.addData("Testing, latest data:  $randomData")
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
