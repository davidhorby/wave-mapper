package com.dhorby.wavemapper.external.google

import com.dhorby.wavemapper.model.GoogleMapLocation

interface MapsClient {
    fun getLocationData(lat:Float, lon:Float): String
    fun getLocationDatAsObject(lat:Float, lon:Float): GoogleMapLocation
}