package com.newdeps.travelmaker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomModel::class], version = 1)
abstract class RoomDB: RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile //변수가 항상 최신 값을 유지함을 보장하는 키워드
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, RoomDB::class.java, "travel_maker_db").build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}