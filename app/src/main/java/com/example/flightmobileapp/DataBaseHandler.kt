package com.example.flightmobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "MyDB.db"
val TABLE_NAME = "Addresses"
val COL_ADDRESS = "address"
val COL_DATE_ENTER = "dateEnter"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ADDRESS + " STRING PRIMARY KEY," +
                COL_DATE_ENTER + " INTEGER)")
        db?.execSQL(createTable)
    }

    fun insertData(localHostAddress: LocalHostAddress) {
        deleteByPrimaryKeyData(localHostAddress.address)
        var db: SQLiteDatabase = writableDatabase
        //val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ADDRESS, localHostAddress.address)
        cv.put(COL_DATE_ENTER, localHostAddress.dateEnter)
        var result = db.insert(TABLE_NAME, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show()
        var lst = readData()
        var i = 0
        while(lst.size - i > 5) {
            deleteData()
            i++
        }
    }

    fun readData(): MutableList<LocalHostAddress> {
        var list: MutableList<LocalHostAddress> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME + " ORDER BY " + COL_DATE_ENTER + " DESC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var localHostAddress = LocalHostAddress()
                localHostAddress.address = result.getString(result.getColumnIndex(COL_ADDRESS))
                localHostAddress.dateEnter =
                    result.getString(result.getColumnIndex(COL_DATE_ENTER)).toLong()
                list.add(localHostAddress)

            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun deleteData() {
        val db = this.readableDatabase
        val query = "Delete from " + TABLE_NAME + " where " +
                COL_DATE_ENTER + " = ( SELECT MIN ( " + COL_DATE_ENTER +
                " ) from " + TABLE_NAME + " )"
        val result = db.execSQL(query)
        db.close()
    }
    fun deleteByPrimaryKeyData(key:String) {
        val db = this.readableDatabase
        db.delete(TABLE_NAME, COL_ADDRESS+"=?", arrayOf(key))
        /*val queryCheck = "Select * from " + TABLE_NAME + " where " + COL_ADDRESS + "=" + key
        val resultCheck = db.rawQuery(queryCheck, null)
        if (resultCheck.moveToFirst()) {
            var address = resultCheck.getString(resultCheck.getColumnIndex(COL_ADDRESS))
            val query = "Delete from " + TABLE_NAME + " where " +
                    COL_ADDRESS + " = " + key
            val result = db.execSQL(query)
        }*/
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        }
        onCreate(db)
    }
}