package com.zinc.streetrestaurant

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.zinc.streetrestaurant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var map: NaverMap
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // 최대 / 최소 줌 사이즈 설정
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance(
                NaverMapOptions()
                    .minZoom(15.0).maxZoom(20.0)
                    .locationButtonEnabled(true)
            ).also {
                supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        setUpView()
    }

    private fun setUpView() {
        binding.findOnClickListener = View.OnClickListener {
            // 검색 화면으로 이동
        }
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
