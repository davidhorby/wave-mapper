package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

data class Site (
    val id:String,
    val geoLocation: GeoLocation,
    val obsLocationType:String,
    val obsRegion:String,
    val obsSource:String,
    val name:String
)

