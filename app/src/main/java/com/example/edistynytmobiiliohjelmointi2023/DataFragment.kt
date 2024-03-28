package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDataBinding


class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.navigateButton.setOnClickListener {
            val action = DataFragmentDirections.actionDataFragmentToDetailFragment(123)
            it.findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding=null
        }
}