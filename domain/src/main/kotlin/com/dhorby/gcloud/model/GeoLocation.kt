package com.dhorby.gcloud.model.com.dhorby.gcloud.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    val lat: Float,
    val lon: Float
)