package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import java.time.LocalDate

enum class BoatType {
    SAIL, TANKER, DINGY
}

data class BoatLocation(
    override val id: String,
    override val name:String,
    override val geoLocation: GeoLocation,
    val date: LocalDate,
    val size: Float,
    val boattype: BoatType
):Location
