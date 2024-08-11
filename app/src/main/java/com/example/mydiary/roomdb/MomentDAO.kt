package com.example.mydiary.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mydiary.model.Moment
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MomentDAO {
    @Query("Select * from Moment")
    fun getAll() : Flowable<List<Moment>>

    @Query("Select * from Moment where Id=:id")
    fun getOneMoment(id:Int) : Flowable<Moment>

    @Insert
    fun insert(moment: Moment) :Completable

    @Delete
    fun delete(moment: Moment) :Completable

}
//Flowable Bir sonuc donekcekse donen sonucu bildirir,arayuze uygular.
//Completable Sonuc donmeyecekse arkaplanda işi tamamlar.
//Rxjava kutuphanesi