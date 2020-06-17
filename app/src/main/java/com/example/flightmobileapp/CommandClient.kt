package com.example.flightmobileapp
import java.io.Serializable

class CommandClient internal constructor(ip: String?, port: Int) : Serializable {
    private val tcp: Client
    fun connect() {
        tcp.openConnection()
    }

    fun setElevator(elevator: Float) {
        val s = StringBuilder()
        s.append(elevatorPath).append(elevator)
            .append(lineEnding)
        val message = s.toString()
        tcp.send(message)
    }

    fun setRudder(aileron: Float) {
        val s = StringBuilder()
        s.append(rudderPath).append(aileron)
            .append(lineEnding)
        val message = s.toString()
        tcp.send(message)
    }
    fun setThrottle(aileron: Float) {
        val s = StringBuilder()
        s.append(throttlePath).append(aileron)
            .append(lineEnding)
        val message = s.toString()
        tcp.send(message)
    }
    fun setAileron(aileron: Float) {
        val s = StringBuilder()
        s.append(aileronPath).append(aileron)
            .append(lineEnding)
        val message = s.toString()
        tcp.send(message)
    }


    fun close() {
        tcp.stopClient()
    }

    companion object {
        private const val elevatorPath = "set controls/flight/elevator "
        private const val aileronPath = "set controls/flight/aileron "
        private const val rudderPath = "set controls/flight/rudder "
        private const val throttlePath = "set controls/flight/throttle "
        private const val lineEnding = "\r\n"
    }

    init {
        tcp = Client(ip, port)
    }
}