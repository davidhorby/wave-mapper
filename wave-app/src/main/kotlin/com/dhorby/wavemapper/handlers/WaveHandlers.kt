package com.dhorby.wavemapper.handlers

import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKeyServer
import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.gcloud.wavemapper.getAllWaveData
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.port.WavePort
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.template.HandlebarsTemplates
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WaveHandlers(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
    val storageAdapter: StoragePort,
    val wavePort: WavePort
) {

    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    companion object {
        val start = PieceLocation(
            id = "NewportR",
            name = "Newport, Rhode Island",
            pieceType = PieceType.START,
            geoLocation = GeoLocation(lat = 41.49, lon = -71.31)
        )
        val finish = PieceLocation(
            id = "Newport",
            name = "Newport, Wales",
            pieceType = PieceType.FINISH,
            geoLocation = GeoLocation(lat = 51.35, lon = -2.59)
        )
    }

    val LOG: Logger = LoggerFactory.getLogger(WaveHandlers::class.java)

    private val devMode = false;
    private val client = ApacheClient()

    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }

    fun getWavePage(): HttpHandler = {

        val waveViewModel = wavePort.getWavePage()
        waveViewModel?.let {
            try {
                Response(OK).body(renderer(waveViewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        } ?: Response(Status.INTERNAL_SERVER_ERROR)

    }

    fun getWaveData(): HttpHandler = {
        val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction, dataForSiteFunction)
        Response(OK).with(waveLocationListBodyLens of allWaveData)
    }


    fun getLocationData(): HttpHandler = {
        val lat = latQuery(it)
        val lon = lonQuery(it)
        val request = Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
            .query("latlng", "$lat,$lon")
            .query("key", mapsApiKeyServer)
        val response = client(request)
        val bodyString = response.bodyString()
        Response(OK).body(bodyString)
    }

    fun addPiece(): HttpHandler = { request ->
        val parametersMap: Map<String, List<String?>> = request.form().toParametersMap()
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
        storageAdapter.add(pieceLocation)
        Response(FOUND).header("Location", "/")
    }


    private fun startRace() {
        storageAdapter.add(start)
        storageAdapter.add(finish)
        val keysOfType: List<PieceLocation> = storageAdapter.getKeysOfType(PIECE_LOCATION, PieceType.BOAT)
        keysOfType
            .map { it.copy(geoLocation = start.geoLocation) }
            .forEach(storageAdapter::add)
    }

    fun start(): HttpHandler = {
        startRace()
        Response(FOUND).header("Location", "/")
    }

    fun move():HttpHandler  = {
        storageAdapter.getKeysOfType(PIECE_LOCATION,PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storageAdapter::add)
        Response(FOUND).header("Location", "/")
    }


    fun clear(): HttpHandler = {
        storageAdapter.clear(PIECE_LOCATION)
        Response(FOUND).header("Location", "/")
    }
}


