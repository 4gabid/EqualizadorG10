package com.grupo10.equalizadorg10.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var volume: Float,
    var bass: Float,
    var middle: Float,
    var treble: Float,
    @ColumnInfo(name = "isLastUsed")
    val isLastUsed: Boolean = false
)
