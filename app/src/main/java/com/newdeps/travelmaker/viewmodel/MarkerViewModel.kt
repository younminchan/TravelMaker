package com.newdeps.travelmaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.map.overlay.Marker

class MarkerViewModel : ViewModel() {
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