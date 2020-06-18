package com.example.flightmobileapp

import androidx.room.*
import retrofit2.http.GET

@Dao
interface UrlDataBaseDao {
    // also works auto-magically: @Update, @Delete
    @Delete
    fun delete(url: UrlData)

    @Query("DELETE FROM urls_table")
    fun delete()
/*    @Delete
    fun delete(url:UrlData)
    @Query("DELETE from urls_table WHERE start_time_milli = (SELECT MIN(start_time_milli) from urls_table)")*/
    //@Insert
/*    fun insert(url: UrlData)
    @Query("SELECT * from urls_table WHERE urlId = :key ORDER BY start_time_milli DESC LIMIT 1")*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg url: UrlData)

    fun getFirstBy(key: Long): UrlData?
/*
    fun GetFirst():UrlData? {
        @Query("SELECT * from urls_table WHERE start_time_milli = (SELECT MAX(start_time_milli)from urls_table)")
    }*/
}