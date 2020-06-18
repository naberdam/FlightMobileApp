package com.example.flightmobileapp

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class UrlDatabase : RoomDatabase() {
    abstract val databaseDap: UrlDataBaseDao

    companion object {
        @Volatile
        private var INSTANCE: UrlDatabase? = null
        fun getInstance(context: Context): UrlDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = newDatabase(context)
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}

fun newDatabase(context: Context): UrlDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        UrlDatabase::class.java,
        "urls_database"
    )
        .fallbackToDestructiveMigration().build()
}