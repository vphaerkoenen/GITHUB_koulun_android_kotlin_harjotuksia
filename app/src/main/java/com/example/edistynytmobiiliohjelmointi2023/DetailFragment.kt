package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding?=null
    private val binding get()=_binding!!
    val args:DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        val root: View = binding.root

        binding.detailTextView.text="Seuraava lukema on lähetetty datafragmentista: "+args.id.toString()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}