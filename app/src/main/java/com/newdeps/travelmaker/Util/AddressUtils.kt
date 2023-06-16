package com.newdeps.travelmaker.Util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.Executors

object AddressUtils {

    private val ioDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    /** 위도,경도 -> 주소 변환 */
    suspend fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        //return withContext(Dispatchers.IO) {
        return withContext(ioDispatcher) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i))
                    if (i < address.maxAddressLineIndex) {
                        sb.append(", ")
                    }
                }
                sb.toString()
            } else {
                "주소를 찾을 수 없습니다."
                //"No address found"
            }
        }
    }
}