package com.dhorby.wavemapper.handlers

import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKey
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKeyServer
import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.gcloud.wavemapper.getAllWaveData
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.WaveServiceRoutes
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.model.GMap
import com.dhorby.wavemapper.model.Wave
import com.dhorby.wavemapper.model.WavePage
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class WaveHandlers(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
    val storageAdapter: StorageAdapter
) {

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

        val theData: String = storageAdapter.getLocationData().toGoogleMapFormat()
        val viewModel: ViewModel? =
            mapsApiKey.let { mapsApiKey ->
                try {
                    WavePage(
                        waveData = theData,
                        mapsApiKey = mapsApiKey,
                        players = storageAdapter.getDistances(),
                        hostname = Settings.HOST,
                        port = Settings.PORT
                    )
                } catch (e: Exception) {
                    LOG.error("Failed to get wave data", e)
                    null
                }
            }
        viewModel?.let {
            try {
                Response(OK).body(renderer(viewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        } ?: Response(Status.INTERNAL_SERVER_ERROR)

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
        Response(OK).with(waveLocationListBodyLens of allWaveData)
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
        storageAdapter.write(pieceLocation)
        Response(FOUND).header("Location", "/")
    }


    private fun startRace() {
        storageAdapter.write(start)
        storageAdapter.write(finish)
        val keysOfType: List<PieceLocation> = storageAdapter.getKeysOfType(PIECE_LOCATION, PieceType.BOAT)
        keysOfType
            .map { it.copy(geoLocation = start.geoLocation) }
            .forEach(storageAdapter::write)
    }

    fun start(): HttpHandler = {
        startRace()
        Response(FOUND).header("Location", "/")
    }

    fun move():HttpHandler  = {
        storageAdapter.getKeysOfType(PIECE_LOCATION,PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storageAdapter::write)
        Response(FOUND).header("Location", "/")
    }


    fun clear(): HttpHandler = {
        storageAdapter.clear(PIECE_LOCATION)
        Response(FOUND).header("Location", "/")
    }
}


