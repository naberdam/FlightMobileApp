package com.example.flightmobileapp

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException


class Client(IP: String?, port: Int) : Serializable {
    // used to send messages
    private var mBufferOut: PrintWriter? = null
    private var serverIP: InetAddress? = null
    private val port: Int
    private var socket: Socket? = null
    fun openConnection() {
        val thread = Thread(Runnable {
            while (socket == null) {
                try {
                    socket = Socket(serverIP, port)
                } catch (e: IOException) {
                    e.message
                }
            }
        })
        thread.start()
    }

    fun send(message: String) {
        val thread = Thread(Runnable {
            try {
                mBufferOut = PrintWriter(
                    BufferedWriter(
                        OutputStreamWriter(
                            socket!!.getOutputStream()
                        )
                    ), true
                )
                Log.d(
                    TAG,
                    "Sending: $message"
                )
                mBufferOut!!.println(message)
                mBufferOut!!.flush()
            } catch (e: Exception) {
                Log.e("TCP", "S: Error", e)
            }
        })
        thread.start()
    }

    fun stopClient() {
        if (mBufferOut != null) {
            mBufferOut!!.flush()
            mBufferOut!!.close()
        }
        try {
            socket!!.close()
        } catch (e: Exception) {
            e.message
        }
        mBufferOut = null
    }

    companion object {
        val TAG = Client::class.java.simpleName
    }


    init {
        try {
            serverIP = InetAddress.getByName(IP)
        } catch (e: UnknownHostException) {
            e.cause
        }
        this.port = port
    }
}