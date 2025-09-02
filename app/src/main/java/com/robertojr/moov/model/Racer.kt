package com.robertojr.moov.model

data class Racer(
    val origin: String,
    val destiny: String,
    val description: String?,
    val driverId: Long,
    val id: Int?,
    val latitudeD: String,
    val longitudeD: String,
    val latitudeO: String,
    val longitudeO: String,
    val pricePerVacancy: Double,
    var racerStatus: String,
    val reservesId: List<Long>?,
    val time: String,
    val vacanciesAvalaible: Int?,
    val vacanciesTotal: Int

) {
    override fun toString(): String {
        return "Racer(description=$description, destiny='$destiny', driverId=$driverId, id=$id, latitudeD='$latitudeD', latitudeO='$latitudeO', longitudeD='$longitudeD', longitudeO='$longitudeO', origin='$origin', pricePerVacancy=$pricePerVacancy, racerStatus='$racerStatus', reservesId=$reservesId, time='$time', vacanciesAvalaible=$vacanciesAvalaible, vacanciesTotal=$vacanciesTotal)"
    }
}