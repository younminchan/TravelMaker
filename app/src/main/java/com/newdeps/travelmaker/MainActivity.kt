package com.newdeps.travelmaker

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.newdeps.travelmaker.databinding.ActivityMainBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import com.newdeps.travelmaker.viewmodel.MarkerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var geocoderViewModel: GeocoderViewModel
    private lateinit var markerViewModel: MarkerViewModel

    lateinit var naverMap: NaverMap
    var clickMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //ViewModel
        geocoderViewModel = ViewModelProvider(this)[GeocoderViewModel::class.java]
        markerViewModel = ViewModelProvider(this)[MarkerViewModel::class.java]

        //DataBinding
        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.mainActivity = this
        binding.geocoderViewModel = geocoderViewModel
        binding.markerViewModel = markerViewModel

        mainActivityInit()
    }

    /** init */
    private fun mainActivityInit() {
        naverMapInit()
    }

    /** NaverMap init */
    private fun naverMapInit() {
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_client_key))

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map_view) as MapFragment? ?: MapFragment.newInstance()
            .also { fm.beginTransaction().add(R.id.naver_map_view, it).commit() }

        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onMapReadyCallback = OnMapReadyCallback() { naverMap ->
        this.naverMap = naverMap.apply {
            mapType = NaverMap.MapType.Basic
            moveCamera(CameraUpdate.zoomTo(17.0))
            addOnCameraIdleListener(cameraIdleListener) //카메라 움직임 종료 시
            setOnMapLongClickListener(cameraLongClick)  //롱 클릭
        }

        //기본 설정
        setObserve()
        moveMap(37.483725, 126.876613)
    }

    /** Observe */
    private fun setObserve() {
        markerViewModel.dbMarkerList.observe(this) { it ->
            it.forEachIndexed { index, roomModel ->
                Log.e("YMC", "index: $index / roomModel: $roomModel")
                setMarker(roomModel.lat, roomModel.lng)
            }
        }
    }

    /** 카메라 롱 클릭*/
    private val cameraLongClick = NaverMap.OnMapLongClickListener { pointF, latLng ->
        var lat = latLng.latitude
        var lng = latLng.longitude
        Toast.makeText(this@MainActivity, "카메라 롱 클릭 / lat: $lat, lng: $lng", Toast.LENGTH_SHORT).show()

        clickMarker.map = null //기존 마커 삭제
        setMarker(lat, lng) //마커 생성
    }
    /** 카메라 움직임 종료 체크 */
    private val cameraIdleListener = NaverMap.OnCameraIdleListener {
        val latitude = naverMap.cameraPosition.target.latitude
        val longitude = naverMap.cameraPosition.target.longitude
        val zoomLevel = naverMap.cameraPosition.zoom

        CoroutineScope(Dispatchers.Main).launch {
            geocoderViewModel.getAddressFromLatLng(applicationContext, latitude, longitude) //위도, 경도 -> 주소 변환
        }
        Log.e("YMC", "cameraIdleListener: $latitude, Longitude: $longitude, Zoom Level: $zoomLevel")
    }

    /** 지도 이동 */
    private fun moveMap(lat: Double, lng: Double) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
        naverMap.moveCamera(cameraUpdate)
    }

    /** 마커 생성 */
    fun setMarker(lat: Double, lng: Double) {
        clickMarker = Marker().apply {
            position = LatLng(lat, lng)
            icon = OverlayImage.fromResource(R.drawable.location_pin)
            width = 100
            height = 100
        }
        clickMarker.map = naverMap
    }

    /** 마커 저장 */
    fun insertMarker() {
        val latitude = naverMap.cameraPosition.target.latitude
        val longitude = naverMap.cameraPosition.target.longitude

        markerViewModel.insertMarker(latitude, longitude)
    }










    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}