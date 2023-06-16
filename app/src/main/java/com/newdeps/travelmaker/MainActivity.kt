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
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.newdeps.travelmaker.databinding.ActivityMainBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var geocoderViewModel: GeocoderViewModel

    lateinit var naverMap: NaverMap
    var clickMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //ViewModel
        geocoderViewModel = ViewModelProvider(this)[GeocoderViewModel::class.java]

        //DataBinding
        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.mainActivity = this
        binding.geocoderViewModel = geocoderViewModel

        mainActivityInit()
    }

    /** init */
    private fun mainActivityInit() {
        naverMapInit()
    }

    /** ViewModel Observe */
    private fun setObserve() {

    }

    /** NaverMap init */
    private fun naverMapInit() {
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_client_key))

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map_view) as MapFragment? ?: MapFragment.newInstance()
            .also { fm.beginTransaction().add(R.id.naver_map_view, it).commit() }

        // MapFragment 준비 완료
        mapFragment.getMapAsync { naverMap ->
            this.naverMap = naverMap

            // 지도 초기화 작업 수행 (ex. 마커 추가, 이벤트 리스너 등록 등)
            this.naverMap.apply {
                mapType = NaverMap.MapType.Basic
                moveCamera(CameraUpdate.zoomTo(17.0))
            }

            //기본 설정
            moveMap(37.483725, 126.876613)
            setMarker(37.483725, 126.876613)
            mapListener()
        }
    }

    /** Listener */
    private fun mapListener() {
        naverMap.apply {
            //카메라 움직임 종료 시
            addOnCameraIdleListener(cameraIdleListener)

            //롱 클릭
            setOnMapLongClickListener { point, coord ->
                Toast.makeText(this@MainActivity, "${coord.latitude}, ${coord.longitude}", Toast.LENGTH_SHORT).show()

                clickMarker.map = null //기존 마커 삭제
                setMarker(coord.latitude, coord.longitude) //마커 생성
            }
        }
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
    private fun setMarker(lat: Double, lng: Double) {
        clickMarker = Marker()
        clickMarker.position = LatLng(lat, lng) //위치설정
        clickMarker.icon = OverlayImage.fromResource(R.drawable.location_pin)
        clickMarker.width = 100
        clickMarker.height = 100

        //clickMarker.icon = MarkerIcons.BLACK //색상
        //clickMarker.iconTintColor = Color.RED //색상 Tint

        clickMarker.map = naverMap
    }





}