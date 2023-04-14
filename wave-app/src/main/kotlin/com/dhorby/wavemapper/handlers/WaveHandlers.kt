package com.dhorby.wavemapper.handlers

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.wavemapper.*
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKey
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKeyServer
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.WaveServiceRoutes
import com.dhorby.wavemapper.model.GMap
import com.dhorby.wavemapper.model.Wave
import com.dhorby.wavemapper.model.WavePage
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.format.Jackson.asJsonObject
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.util.*

class WaveHandlers(val siteListFunction: SiteListFunction, val dataForSiteFunction: DataForSiteFunction) {

    private val devMode = false;

    private val client = ApacheClient()

    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }

    fun getWavePage(): HttpHandler = {

        val viewModel: ViewModel =
            mapsApiKey.let { mapsApiKey ->
                val waveData: String =
                    getAllWaveData(siteListFunction = siteListFunction, dataForSiteFunction)
                        .withStoredSharks()
                        .withStoredBoats()
                        .toGoogleMapFormat()
                WavePage(waveData, mapsApiKey)
            }


        viewModel.let {
            try {
                Response(OK).body(renderer(viewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        }

    }

    fun getMap(): HttpHandler = {

        GMap(mapsApiKey).let {
            try {
                Response(OK).body(renderer(it))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        }

    }

    fun getWaveData(): HttpHandler = {
        val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction, dataForSiteFunction)
        Response(OK).with(waveLocationListBodyLens() of allWaveData)
    }

    fun getProperties(): HttpHandler = {
        val properties: Properties = System.getProperties()
        val allProperties = properties.filter { it.key != null }.map {
            it.key.toString() + ":" + it.value
        }.joinToString("</br>")
        Response(OK).body(allProperties)
    }

    fun getDataSheet(): HttpHandler = {
        val (renderer, _) = buildResourceLoaders(false)
        val viewModel = Wave("M5", 2.1.toLong())
        Response(OK).body(renderer(viewModel))
    }

    private fun buildResourceLoaders(hotReload: Boolean) = when {
        hotReload -> HandlebarsTemplates().HotReload("./src/main/resources") to ResourceLoader.Classpath("public")
        else -> HandlebarsTemplates().CachingClasspath() to ResourceLoader.Classpath("public")
    }

    fun getLocationData(): HttpHandler = {
        val lat = WaveServiceRoutes.latQuery(it)
        val lon = WaveServiceRoutes.lonQuery(it)
        val request = Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
            .query("latlng", "$lat,$lon")
            .query("key", mapsApiKeyServer)
        val response = client(request)
        val bodyString = response.bodyString()
        Response(OK).body(bodyString)
    }

    fun addPiece(): HttpHandler = { request ->
        val parametersMap = request.form().toParametersMap()
        val name = parametersMap["name"]?.first() ?: "Unknown"
        val pieceType = PieceType.valueOf(parametersMap["pieceType"]?.first() ?: "UNKNOWN")
        val lat = parametersMap["lat"]?.first()?.toDouble() ?: 0.0
        val lon = parametersMap["lon"]?.first()?.toDouble() ?: 0.0

        val pieceLocation = PieceLocation(
            id = name,
            name = name,
            pieceType = pieceType,
            geoLocation = GeoLocation(lat = lat, lon = lon)
        )
        pieceLocation.asJsonObject().textValue()
        val addRequest = Request(Method.POST, "http://localhost:8080/sg-http-to-bucket")
            .body(pieceLocation.asJsonObject().toString())
        when (client(addRequest).status) {
            OK -> Response(FOUND).header("Location", "/")
            else -> client(addRequest)
        }
    }

    fun clear(): HttpHandler =  {
        val deleteRequest = Request(Method.DELETE, "http://localhost:8082/clear-firestore")
        when (client(deleteRequest).status) {
            OK -> Response(FOUND).header("Location", "/")
            else -> client(deleteRequest)
        }
    }
}
