package com.zinc.streetrestaurant

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.naver.maps.map.overlay.InfoWindow

class InfoWindowAdapter(private val context: Context) : InfoWindow.ViewAdapter() {
        private var rootView: View? = null
        private var icon: ImageView? = null
        private var text: TextView? = null

        override fun getView(infoWindow: InfoWindow): View {
            val view = rootView ?: View.inflate(context, R.layout.view_custom_info_window, null).also { rootView = it }
            val icon = icon ?: view.findViewById<ImageView>(R.id.icon).also { icon = it }
            val text = text ?: view.findViewById<TextView>(R.id.text).also { text = it }

            icon.setImageResource(R.drawable.shop)
            text.text = "여기에 추가"

            return view
        }
    }