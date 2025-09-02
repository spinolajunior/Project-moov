package com.robertojr.moov.model.sending

data class RacerSending(
    val description: String,
    val destiny: String,
    val driver: Driver,
    val latitudeD: String,
    val latitudeO: String,
    val longitudeD: String,
    val longitudeO: String,
    val origin: String,
    val pricePerVacancy: Double,
    val racerStatus: Int,
    val time: String,
    val vacancies: String
) {
    override fun toString(): String {
        return "RacerSending(description='$description', destiny='$destiny', driver=$driver, latitudeD='$latitudeD', latitudeO='$latitudeO', longitudeD='$longitudeD', longitudeO='$longitudeO', origin='$origin', pricePerVacancy=$pricePerVacancy, racerStatus=$racerStatus, time='$time', vacancies='$vacancies')"
    }
}