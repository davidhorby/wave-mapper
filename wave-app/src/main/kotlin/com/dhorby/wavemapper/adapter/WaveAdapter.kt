package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKey
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.components.WavePage
import com.dhorby.wavemapper.external.google.GoogleMapsClient
import com.dhorby.wavemapper.external.metoffice.MetOfficeClient
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.port.WavePort
import org.http4k.template.ViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WaveAdapter(
    val storageAdapter: StoragePort,
    val googleMapsClient: GoogleMapsClient,
    val metOfficeClient: MetOfficeClient
) : WavePort {
    val LOG: Logger = LoggerFactory.getLogger(WaveHandlers::class.java)

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
                        port = Settings.PORT,
                        template = "/templates/WavePage"
                    )
                } catch (e: Exception) {
                    LOG.error("Failed to get wave data", e)
                    null
                }
            }
        return viewModel
    }


    override fun getWaveData(): MutableList<Location> = metOfficeClient.getSiteList()

    override fun getLocationData(lat: Float, lon: Float): String = googleMapsClient.getLocationData(lat, lon)

    override fun addPiece(parametersMap: Map<String, List<String?>>): Boolean {
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


    override fun startRace() {
        storageAdapter.add(start)
        storageAdapter.add(finish)
        val keysOfType: List<PieceLocation> = storageAdapter.getKeysOfType(PIECE_LOCATION, PieceType.BOAT)
        keysOfType
            .map { it.copy(geoLocation = start.geoLocation) }
            .forEach(storageAdapter::add)
    }


    override fun move() =
        storageAdapter.getKeysOfType(PIECE_LOCATION, PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storageAdapter::add)


    override fun clear() = storageAdapter.clear(PIECE_LOCATION)
}