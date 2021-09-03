package com.dhorby.wavemapper.model


data class Location(
    val id: String,
    val name: String,
    val lat: Float,
    val lon: Float,
    val datePeriods: List<DatePeriod>
)

