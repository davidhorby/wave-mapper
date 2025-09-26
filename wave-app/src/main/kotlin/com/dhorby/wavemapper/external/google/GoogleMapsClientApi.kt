package com.dhorby.wavemapper.external.google

import com.dhorby.gcloud.wavemapper.Constants.mapsApiKeyServer
import com.dhorby.wavemapper.model.GoogleMapLocation
import com.dhorby.wavemapper.model.googleMapLocationLens
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class GoogleMapsClientApi(
    val client: HttpHandler,
) : MapsClient {
    override fun getLocationData(
        lat: Float,
        lon: Float,
    ): String {
        val request =
            Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
//        val request = Request(Method.GET, "/maps/api/geocode/json")
                .query("latlng", "$lat,$lon")
                .query("key", mapsApiKeyServer)
        val response = client(request)
        googleMapLocationLens.extract(response)
        return response.bodyString()
    }

    override fun getLocationDatAsObject(
        lat: Float,
        lon: Float,
    ): GoogleMapLocation {
        val request =
            Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
                .query("latlng", "$lat,$lon")
                .query("key", mapsApiKeyServer)
        val response = client(request)
        return googleMapLocationLens.extract(response)
    }
}
