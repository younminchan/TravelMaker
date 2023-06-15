package com.newdeps.travelmaker

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.newdeps.travelmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var naverMap: NaverMap
    var clickMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       naverMapInit()
    }

    private fun naverMapInit() {
        // Initialize NaverMapSdk with your API key
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_client_key))

        val fm = supportFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.naver_map_view) as MapFragment? ?: MapFragment.newInstance()
                .also {
                    fm.beginTransaction().add(R.id.naver_map_view, it).commit()
                }

        // MapFragment가 준비되면 지도 초기화 작업 수행
        mapFragment.getMapAsync { naverMap ->
            this.naverMap = naverMap

            // 지도 초기화 작업 수행 (ex. 마커 추가, 이벤트 리스너 등록 등)
            this.naverMap.apply {
                mapType = NaverMap.MapType.Basic
                moveCamera(CameraUpdate.zoomTo(17.0))
            }

            moveMap(37.483725, 126.876613)
            setMarker(37.483725, 126.876613)
            mapClickListener()
        }
    }
    private fun mapClickListener() {
        /** 롱클릭 마커 표시 */
        naverMap.setOnMapLongClickListener { point, coord ->
            Toast.makeText(this, "${coord.latitude}, ${coord.longitude}", Toast.LENGTH_SHORT).show()

            clickMarker.map = null //기존 마커 삭제
            setMarker(coord.latitude, coord.longitude)
        }
    }

    /** 지도 이동 */
    private fun moveMap(lat: Double, lng: Double) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
        naverMap.moveCamera(cameraUpdate)
    }

    /** 마커 생성 */
    private fun setMarker(lat: Double, lng: Double) {
        clickMarker = Marker()
        clickMarker.position = LatLng(lat, lng) //위치설정
        clickMarker.icon = MarkerIcons.BLACK //색상
        clickMarker.iconTintColor = Color.RED //색상 Tint
        clickMarker.map = naverMap
    }



}