package com.robertojr.util.dto

import com.robertojr.moov.model.Reserve

data class SessionUserDTO(
    val ager: Int?,
    val type: String?,
    val available: String?,
    val carModel: String?,
    val credentialId: Long,
    val description: String?,
    val id: Long,
    val lastName: String?,
    val name: String?,
    val phone: String?,
    val photoProfile: String?,
    val plateNumber: String?,
    val racersId: List<Long?>?,
    val ratingNumber: Double?,
    val reserves: List<Reserve?>?


) {
    override fun toString(): String {
        return "SessionUserDTO(ager=$ager, available=$available, carModel=$carModel, credentialId=$credentialId, description=$description, id=$id, type=$type, lastName=$lastName, name=$name, phone=$phone, photoProfile=$photoProfile, plateNumber=$plateNumber, racersId=$racersId, ratingNumber=$ratingNumber, reserves=$reserves)"
    }
}