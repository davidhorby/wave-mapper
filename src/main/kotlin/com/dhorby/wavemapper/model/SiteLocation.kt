package com.dhorby.wavemapper.model

data class SiteLocation(
    val id:String,
    val latitude:Float,
    val longitude:Float,
    val obsLocationType:String,
    val obsRegion:String,
    val obsSource:String,
    val name:String
)
