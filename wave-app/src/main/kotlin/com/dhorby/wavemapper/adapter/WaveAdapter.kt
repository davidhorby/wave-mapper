package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.config.Settings
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
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.model.WavePage
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.port.WavePort
import org.http4k.client.ApacheClient
import org.http4k.client.ApacheClient.invoke
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.template.ViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WaveAdapter(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
    val storageAdapter: StoragePort
): WavePort {
    val LOG: Logger = LoggerFactory.getLogger(WaveHandlers::class.java)

    override fun getWavePage(): ViewModel? {
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
        return viewModel
    }


     override fun getWaveData():MutableList<Location> = getAllWaveData(siteListFunction, dataForSiteFunction)


    private val client = ApacheClient()

    override fun getLocationData(lat:Float, lon:Float): String  {
        val request = Request(Method.GET, "https://maps.googleapis.com/maps/api/geocode/json")
            .query("latlng", "$lat,$lon")
            .query("key", mapsApiKeyServer)
        val response = client(request)
        return response.bodyString()
    }

    override fun addPiece(parametersMap: Map<String, List<String?>>): Boolean  {
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
        return true
    }
//
//
//    private fun startRace() {
//        storageAdapter.add(WaveHandlers.Companion.start)
//        storageAdapter.add(finish)
//        val keysOfType: List<PieceLocation> = storageAdapter.getKeysOfType(PIECE_LOCATION, PieceType.BOAT)
//        keysOfType
//            .map { it.copy(geoLocation = WaveHandlers.Companion.start.geoLocation) }
//            .forEach(storageAdapter::add)
//    }
//
//    fun start(): HttpHandler = {
//        startRace()
//        Response(FOUND).header("Location", "/")
//    }
//
//    fun move():HttpHandler  = {
//        storageAdapter.getKeysOfType(PIECE_LOCATION,PieceType.BOAT)
//            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
//            .forEach(storageAdapter::add)
//        Response(FOUND).header("Location", "/")
//    }
//
//
//    fun clear(): HttpHandler = {
//        storageAdapter.clear(PIECE_LOCATION)
//        Response(FOUND).header("Location", "/")
//    }
}