package com.newdeps.travelmaker.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newdeps.travelmaker.Util.AddressUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeocoderViewModel : ViewModel() {
    //위도,경도 -> 주소
    private val _addressResult = MutableLiveData<String>()
    val addressResult: LiveData<String> get() = _addressResult

    suspend fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double) {
        try {
            val address = withContext(Dispatchers.IO) {
                AddressUtils.getAddressFromLatLng(context, latitude, longitude)
            }
            //Log.e("YMC", "address: $address")
            _addressResult.postValue(address)
        } catch (e: Exception) {
            _addressResult.postValue(null)
        }
    }
}