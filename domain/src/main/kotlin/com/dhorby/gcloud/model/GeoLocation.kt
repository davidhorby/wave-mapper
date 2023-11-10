package com.dhorby.gcloud.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    val lat: Double,
    val lon: Double
)
