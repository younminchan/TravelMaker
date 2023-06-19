package com.newdeps.travelmaker.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RoomDao {
    @Query("SELECT * FROM travel_maker")
    fun getAllMarker(): LiveData<List<RoomModel>>

    @Insert(onConflict = REPLACE)
    fun insertMarker(roomModel: RoomModel)
    //onConflict = REPLACE: 삽입할 데이터가 충돌되었을 경우 기존 데이터를 바꿔치기하는 역할
    //데이터 병합이나 업데이트와 같이 데이터 중복이 발생할 때 유용하게 사용할 수 있습니다.

    @Delete
    fun deleteMarker(roomModel: RoomModel)
//
//    @Query("DELETE FROM mvvm WHERE id = :id")
//    fun deleteItem2(id: Int)
//
//    @Query("DELETE FROM mvvm")
//    fun deleteAll()

}