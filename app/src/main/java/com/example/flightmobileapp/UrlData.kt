package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "urls_table")
data class UrlData(
    @PrimaryKey(autoGenerate = true)
    var urlId: Long = 0L,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time_milli")
    var urlTime: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "quality_rating")
    var ipAndPort: String = ""
)