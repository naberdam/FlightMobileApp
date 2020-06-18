package com.example.flightmobileapp

class LocalHostAddress {
    var address : String = ""
    var dateEnter : Long = System.currentTimeMillis()

    constructor()

    constructor(address:String, dateEnter:Long) {
        this.address = address
        this.dateEnter = dateEnter
    }
}