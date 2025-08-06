package com.robertojr.moov.model

class Driver() : User() {

    var carModel: String? = null
    var plateNumber: String? = null

    var available: String? = null
    var ratingNumber: Double? = null
    var racersId: List<Int>? = emptyList()

    constructor(id: Long?, name: String, lastName: String, phone: String, ager: Int?, description: String?, photoProfile: String?,
        credentialId: Long?, credential: Login?, carModel: String, plateNumber: String, ratingNumber: Double?, available: String?, racersId: List<Int>?
    ) : this() {
        this.id = id
        this.name = name
        this.lastName = lastName
        this.phone = phone
        this.ager = ager
        this.description = description
        this.photoProfile = photoProfile
        this.credential = credential
        this.credentialId = credentialId
        this.carModel = carModel
        this.plateNumber = plateNumber
        this.ratingNumber = ratingNumber
        this.available = available
        this.racersId = racersId

    }
}
