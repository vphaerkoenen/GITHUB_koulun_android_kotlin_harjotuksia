package com.example.edistynytmobiiliohjelmointi2023.ui.gallery

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.edistynytmobiiliohjelmointi2023.R
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.imageViewRobotAnimation.setBackgroundResource(R.drawable.roboanimation)
        val frameAnimation = binding.imageViewRobotAnimation.background as AnimationDrawable
        frameAnimation.start()





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}