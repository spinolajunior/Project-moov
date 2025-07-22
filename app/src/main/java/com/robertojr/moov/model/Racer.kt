package com.robertojr.moov.model

data class Racer(
    val description: String,
    val destiny: String,
    val driverId: Int,
    val id: Int,
    val latitudeD: String,
    val latitudeO: String,
    val longitudeD: String,
    val longitudeO: String,
    val origin: String,
    val pricePerVacancy: Double,
    val racerStatus: String,
    val reservesId: List<Int>,
    val time: String,
    val vacanciesAvalaible: Int,
    val vacanciesTotal: Int
)