package com.example.letschat.model

class Message {

    var messageId: String? = null
    var message: String? = null
    var senderId: String? = null
    var imageurl: String? = null
    var timestamp: Long = 0

    constructor(){}
    constructor(
        message: String?,
        senderId: String?,
        timestamp: Long
    ){
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
    }

}