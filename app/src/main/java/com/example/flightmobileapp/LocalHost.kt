package com.example.flightmobileapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocalHosts")
data class LocalHost (
    @PrimaryKey
    val id: Int,
    val localHost: String
)