package com.robertojr.moov.model


open class User() {
    var ager: Int? = null
    var credentialId: Long? = null

    var credential: Login? = null
    var description: String? = null
    var id: Long? = null
    var lastName: String? = null
    var name: String? = null
    var phone: String? = null
    var photoProfile: String? = null

    constructor(
        ager: Int,
        credentialId: Long,
        credential: Login,
        description: String,
        id: Long,
        lastName: String,
        name: String,
        phone: String,
        photoProfile: String
    ) : this() {
        this.id = id
        this.ager = ager
        this.credentialId = credentialId
        this.description = description
        this.lastName = lastName
        this.name = name
        this.phone = phone
        this.photoProfile = photoProfile
        this.credential = credential

    }
}

