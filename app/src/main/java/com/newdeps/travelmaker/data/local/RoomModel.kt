package com.newdeps.travelmaker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_maker")
class RoomModel(
    @PrimaryKey(autoGenerate = true) var idx: Int,
    @ColumnInfo(name = "lat") var lat: Double,
    @ColumnInfo(name = "lng") var lng: Double,
) {
    constructor() : this(0, 0.0, 0.0)
}