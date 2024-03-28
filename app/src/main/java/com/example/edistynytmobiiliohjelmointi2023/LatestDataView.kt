package com.example.edistynytmobiiliohjelmointi2023

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

class LatestDataView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var maxRows = 5


    // rest of the basic methods here from the template above

    init{
        this.orientation= VERTICAL

        // säädetään jotta textvievin korkeus on sopiva viidelle viestille.
        // tehdään textview jota ei käytetä kuin yhden textvievin mittaamiseen,
        // tämän jälkeen kerrotaan saatu mittaus rivimäärällä jotta saadaan oikea korkeus

        var someTextView:TextView= TextView(context)
        someTextView.measure(0,0)
        var rowHeight = someTextView.measuredHeight

        this.measure(0,0)
        var additionalHeight = this.measuredHeight+(maxRows*rowHeight)
        this.minimumHeight=additionalHeight
        this.setBackgroundColor(Color.BLACK)


        addData("testiviesti 1")
        addData("testiviesti 2")
    }
    // this function can be called where it's needed, init() or an Activity.
    fun addData(message : String)
    {
        // Aluksi säädetään että textview-korkeus on sopiva viidelle viestille
        while (this.childCount >= maxRows){
            this.removeViewAt(0)
        }

        var newTextView : TextView = TextView(context) as TextView
        newTextView.setText(message)
        newTextView.setBackgroundColor(Color.BLACK)
        newTextView.setTextColor(Color.YELLOW)
        this.addView(newTextView)

        //loading our custom made animations
        val animation = AnimationUtils.loadAnimation(context, R.anim.customfade)
        //starting the animation
        newTextView.startAnimation(animation)
    }
}