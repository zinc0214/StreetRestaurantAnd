package com.zinc.streetrestaurant

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.zinc.streetrestaurant.databinding.ActivitySelectNewRestaurantPositionBinding

class SelectNewShopPositionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var map: NaverMap
    private lateinit var binding: ActivitySelectNewRestaurantPositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_new_restaurant_position)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // 최대 / 최소 줌 사이즈 설정
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance(
                NaverMapOptions()
                    .minZoom(10.0).maxZoom(20.0)
                    .locationButtonEnabled(true)
            ).also {
                supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(naverMap: NaverMap) {

        //init
        map = naverMap
        naverMap.locationSource = locationSource

        // gps
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.addOnOptionChangeListener {
            Log.e("ayhan", "addOnOptionChangeListener")
            val mode = naverMap.locationTrackingMode
            locationSource.isCompassEnabled =
                mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face
        }

        // 마커
        Marker().apply {
            position = LatLng(37.56768, 126.98602)
            icon = OverlayImage.fromResource(R.drawable.pizza)
            anchor = PointF(1f, 1f)
            angle = 90f
            map = naverMap
        }

        val infoWindow = InfoWindow().apply {
            anchor = PointF(0f, 1f)
            offsetX = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_x)
            offsetY = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_y)
            adapter = InfoWindowAdapter(this@SelectNewShopPositionActivity)
            setOnClickListener {
                binding.addButton.isEnabled = false
                close()
                true
            }
        }

        // click Event
        naverMap.setOnMapClickListener { pointF, latLng ->
            Log.e("ayhan", "setOnMapClickListener")
            infoWindow.position = latLng
            infoWindow.open(naverMap)

            binding.addButton.isEnabled = true
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                map.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
