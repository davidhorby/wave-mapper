package com.dhorby.wavemapper.model

import java.time.LocalDate

enum class BoatType {
    SAIL, TANKER, DINGY
}

data class BoatLocation(
    override val id: String,
    override val name:String,
    val date: LocalDate,
    val lat: Float,
    val lon: Float,
    val size: Float,
    val boattype: BoatType
):Location
