package com.dhorby.wavemapper.model

import org.http4k.core.Body
import org.http4k.format.Jackson.auto


data class Location(
    val id: String,
    val name: String,
    val lat: Float,
    val lon: Float,
    val datePeriods: List<DatePeriod>
)

val locationBodyLens = Body.auto<Location>().toLens()
val locationListBodyLens = Body.auto<List<Location>>().toLens()

