package com.robertojr.moov.model

class Customer() : User() {

    var reserves: List<Reserve>? = null

    constructor(
        id: Long?, name: String, lastName: String, ager: Int?, credentialId: Long?, credential: Login?,
        phone: String?, photoProfile: String?, description: String?, reserves: List<Reserve>?
    ) : this() {
        this.id = id
        this.name = name
        this.lastName = lastName
        this.ager = ager
        this.credentialId = credentialId
        this.credential = credential
        this.phone = phone
        this.photoProfile = photoProfile
        this.description = description
        this.reserves = reserves

    }
}


