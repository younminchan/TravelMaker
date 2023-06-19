package com.newdeps.travelmaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.newdeps.travelmaker.App
import com.newdeps.travelmaker.data.local.RoomDB
import com.newdeps.travelmaker.data.local.RoomModel
import com.newdeps.travelmaker.data.repository.TravelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarkerViewModel : ViewModel() {
    private var repository: TravelRepository
    private var roomDao = RoomDB.getInstance(App.context).roomDao()
    init {
        repository = TravelRepository(roomDao)
    }


    /** DB연동 */
    //DB에 저장된 마커 리스트
    private val _dbMarkerList = MutableLiveData<List<RoomModel>>(emptyList())
    val dbMarkerList: LiveData<List<RoomModel>> = repository.getAllMarker()
    fun insertMarker(lat:Double, lng: Double) {
        var markerModel = RoomModel(0, lat, lng)
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertMarker(markerModel)
        }
    }


    //마커 리스트
    private val _markerList = MutableLiveData<List<Marker>>(emptyList())
    val markerList: LiveData<List<Marker>> = _markerList

    fun addMarker(marker: Marker) {
        //TODO: orEmpty는 null일때 빈 list를 반환
        val currentList = _markerList.value.orEmpty().toMutableList()
        currentList.add(marker)
        _markerList.value = currentList
    }
}