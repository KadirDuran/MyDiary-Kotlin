package com.example.mydiary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Moment (

    @ColumnInfo("Title")
    var title : String,

    @ColumnInfo("Moment")
    var momenttxt : String,

    @ColumnInfo("Image")
    var image : ByteArray
){
    @PrimaryKey(autoGenerate = true)
    var id = 0 //Otomatik Id tanımlaması.
}