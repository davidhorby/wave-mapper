package com.dhorby.wavemapper.model

import java.time.LocalDate

enum class SharkType {
    HAMMERHEAD, GREAT_WHITE, BASKING, TIGER, WHALE
}

data class SharkLocation(
    override val id: String,
    override val name:String,
    val date: LocalDate,
    val lat: Float,
    val lon: Float,
    val size: Float,
    val species: SharkType
):Location
