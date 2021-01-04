package com.agh.engineeringthesis.eCar.util

import android.content.Context
import android.widget.TextView
import com.agh.engineeringthesis.eCar.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight


class CustomMarkerView(context: Context, layoutResource: Int, fragment: String) : MarkerView(
    context,
    layoutResource
) {

    private val first: TextView = findViewById(R.id.first_row)
    private val mfragment = fragment
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        when (mfragment){
            "spalanie" -> {
                first.text = String.format("Spalanie: %.2f l/100km", e!!.y)
            }
            "cena paliwa" -> {
                first.text = String.format("Cena paliwa: %.2f zł/l", e!!.y)
            }
            "przebieg" -> {
                first.text = String.format("Przebieg: %.2f km", e!!.y)
            }
        }

        super.refreshContent(e, highlight)
    }
}
