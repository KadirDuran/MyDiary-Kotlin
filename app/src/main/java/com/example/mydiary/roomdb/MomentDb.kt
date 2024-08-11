package com.example.mydiary.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mydiary.model.Moment

@Database(entities= [Moment :: class],version=1)
abstract class MomentDb : RoomDatabase() {
    abstract fun MomentDAO(): MomentDAO
}