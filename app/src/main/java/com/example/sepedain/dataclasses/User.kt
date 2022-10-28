package com.example.sepedain.dataclasses

class User {
    var username: String? = null
    var email: String? = null
    var uid: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var phoneNumber: String? = null
    var gender: String? = null

    constructor()
    constructor(username: String?, email: String?, uid: String?, ) {
        this.username = username
        this.email = email
        this.uid = uid
        this.firstName = "User"
    }

}