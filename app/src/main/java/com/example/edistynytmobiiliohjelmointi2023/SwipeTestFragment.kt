package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentSwipeTestBinding


class SwipeTestFragment : Fragment() {
    private var _binding : FragmentSwipeTestBinding?=null
    private val binding get()=_binding!!
  //  val args:DetailFragmentArgs by navArgs()

    private lateinit var layout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwipeTestBinding.inflate(inflater,container,false)
        val root: View = binding.root
        //setContentView(R.layout.fragment_swipe_test)
        layout = binding.relativeLayout

        layout.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Toast.makeText(context, "Swipe Left gesture detected",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                Toast.makeText(
                    context,
                    "Swipe Right gesture detected",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onSwipeUp() {
                super.onSwipeUp()
                Toast.makeText(context, "Swipe up gesture detected", Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onSwipeDown() {
                super.onSwipeDown()
                Toast.makeText(context, "Swipe down gesture detected", Toast.LENGTH_SHORT)
                    .show()
            }

        })
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}

