package com.newdeps.travelmaker.data.repository

import com.newdeps.travelmaker.data.local.RoomDao
import com.newdeps.travelmaker.data.local.RoomModel

class TravelRepository(private var roomDao: RoomDao) {
    fun getAllMarker() = roomDao.getAllMarker()
    fun insertMarker(roomModel: RoomModel) = roomDao.insertMarker(roomModel)
}