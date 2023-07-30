package com.newdeps.travelmaker.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.newdeps.travelmaker.R
import com.newdeps.travelmaker.databinding.FragmentRecommendBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import com.newdeps.travelmaker.viewmodel.MarkerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendFragment : Fragment() {
    lateinit var binding: FragmentRecommendBinding
    private lateinit var geocoderViewModel: GeocoderViewModel
    private lateinit var markerViewModel: MarkerViewModel

    lateinit var naverMap: NaverMap
    var clickMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recommend, container, false)

        //ViewModel
        geocoderViewModel = ViewModelProvider(requireActivity())[GeocoderViewModel::class.java]
        markerViewModel = ViewModelProvider(requireActivity())[MarkerViewModel::class.java]

        //DataBinding
        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.recommendFragment = this
        binding.geocoderViewModel = geocoderViewModel
        binding.markerViewModel = markerViewModel

        mainActivityInit()

        return binding.root
    }

    /** init */
    private fun mainActivityInit() {
        naverMapInit()
    }

    /** NaverMap init */
    private fun naverMapInit() {
        NaverMapSdk.getInstance(requireActivity()).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_client_key))

        //val fm = requireActivity().supportFragmentManager
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map_view) as MapFragment? ?: MapFragment.newInstance()
            .also { fm.beginTransaction().add(R.id.naver_map_view, it).commit() }

        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onMapReadyCallback = OnMapReadyCallback() { naverMap ->
        Log.e("YMC", "onMapReadyCallback")
        this.naverMap = naverMap.apply {
            mapType = NaverMap.MapType.Basic
            moveCamera(CameraUpdate.zoomTo(17.0))
            addOnCameraIdleListener(cameraIdleListener) //카메라 움직임 종료 시
            //setOnMapLongClickListener(cameraLongClick)  //롱 클릭
            onMapLongClickListener = cameraLongClick
            onSymbolClickListener = symbolListener
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
        clickMarker.map = null //기존 마커 삭제

        var lat = latLng.latitude
        var lng = latLng.longitude
        setMarker(lat, lng) //마커 생성

        Toast.makeText(requireActivity(), "카메라 롱 클릭 / lat: $lat, lng: $lng", Toast.LENGTH_SHORT).show()
    }
    /** 카메라 움직임 종료 체크 */
    private val cameraIdleListener = NaverMap.OnCameraIdleListener {
        val latitude = naverMap.cameraPosition.target.latitude
        val longitude = naverMap.cameraPosition.target.longitude
        val zoomLevel = naverMap.cameraPosition.zoom

        CoroutineScope(Dispatchers.Main).launch {
            //위도,경도 -> 주소 변환
            geocoderViewModel.getAddressFromLatLng(requireActivity(), latitude, longitude) //위도, 경도 -> 주소 변환
        }
        Log.e("YMC", "--cameraIdleListener: $latitude, Longitude: $longitude, Zoom Level: $zoomLevel")
    }

    private val symbolListener = NaverMap.OnSymbolClickListener { symbol ->
        Log.e("YMC", "symbol: $symbol")
        Toast.makeText(requireActivity(), "symbol.caption: ${symbol.caption} / symbol.position: ${symbol.position}", Toast.LENGTH_SHORT).show()
        true
    }

    /** 지도 이동 */
    private fun moveMap(lat: Double, lng: Double) {
        Log.e("YMC", "moveMap lat: $lat / lng: $lng")
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
        naverMap.moveCamera(cameraUpdate)
    }

    /** 마커 생성 */
    fun setMarker(lat: Double, lng: Double) {
        Marker().apply {
            position = LatLng(lat, lng)
            icon = OverlayImage.fromResource(R.drawable.location_pin)
            width = 100
            height = 100
        }.map = naverMap
    }

    /** 마커 저장 */
    fun insertMarker() {
        val latitude = naverMap.cameraPosition.target.latitude
        val longitude = naverMap.cameraPosition.target.longitude

        markerViewModel.insertMarker(latitude, longitude)
    }
}