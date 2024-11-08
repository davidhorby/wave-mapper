package com.dhorby.wavemapper.external.google

import com.dhorby.gcloud.wavemapper.Constants.mapsApiKeyServer
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Request.Companion.invoke

class GoogleMapsClient(val client: HttpHandler) {
    fun getLocationData(lat:Float, lon:Float): String  {
        val request = Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
            .query("latlng", "$lat,$lon")
            .query("key", mapsApiKeyServer)
        val response = client(request)
        return response.bodyString()
    }
}