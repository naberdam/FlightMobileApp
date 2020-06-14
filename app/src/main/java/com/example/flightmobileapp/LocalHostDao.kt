package com.example.flightmobileapp

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface LocalHostDao {
    @Insert
    fun insert(localHost: LocalHost)
    @Update
    fun update(localHost: LocalHost)
    @Delete
    fun delete(localHost: LocalHost)
    @Query("SELECT * FROM LocalHosts" +
            " WHERE id=:id")
    fun findLocalHostById(id:Int) : List<LocalHost>
    @Query("SELECT * FROM LocalHosts")
    fun getAllLocalHosts()
    @RawQuery
    fun getLocalHostViaQuery(query:SupportSQLiteQuery)
}