package com.dhorby.gcloud.model

data class Site (
    val id:String,
    val geoLocation: GeoLocation,
    val obsLocationType:String,
    val obsRegion:String,
    val obsSource:String,
    val name:String
)

