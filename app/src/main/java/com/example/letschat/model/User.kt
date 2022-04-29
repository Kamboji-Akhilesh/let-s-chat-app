package com.example.letschat.model

class User {
    var uid : String? = null
    var name : String? = null
    var phonenumber : String? = null
    var profileimage : String? = null
    constructor(){}
    constructor(
        uid:String?,
        name:String?,
        phonenumber:String?,
        profileimage:String?
    ){
        this.uid = uid
        this.name = name
        this.phonenumber = phonenumber
        this.profileimage = profileimage
    }

}