package com.dhorby.gcloud.wavemapper

import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.external.storage.DatastoreKind.PIECE_LOCATION_KIND
import com.dhorby.gcloud.external.storage.toPieceLocation
import com.dhorby.gcloud.model.*
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat


interface WaveDataActions {
    fun clear(kind: String)
    fun write(pieceLocation: PieceLocation)
    fun getKeysOfKind(kind: String): List<PieceLocation>
    fun getAllLocations(kind: String): List<PieceLocation>
    fun getAllWaveData(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location>

    fun getWaveDataOnly(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): String
    fun getAllWaveDataWithPieces(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location>

    fun loadDistance(pieceLocation: PieceLocation, pieceType: PieceType): List<Distance>
    fun getKeysOfType(kind: String, type: PieceType): List<PieceLocation>
}

interface WaveDataCalculations {
    fun getDistances(): List<Player>
}

class DataStorage(private val dataStoreClient: DataStoreClient) : WaveDataActions, WaveDataCalculations {

    override fun clear(kind: String) {
        dataStoreClient.clearDatastore(kind)
    }

    override fun write(pieceLocation: PieceLocation) {
        this.dataStoreClient. writeToDatastore(PIECE_LOCATION_KIND, pieceLocation)
    }

    override fun getKeysOfKind(kind: String): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfKind(kind).map {
            it.toPieceLocation()
        }
    }

    override fun getKeysOfType(kind: String, type:PieceType): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfType(kind, type).map {
            it.toPieceLocation()
        }
    }


    override fun getAllLocations(kind: String): List<PieceLocation> = getKeysOfKind(kind)

    override fun getDistances(): List<Player> {
        val boats = dataStoreClient.getAllEntitiesOfType( "PieceLocation", PieceType.BOAT).map {
            it.toPieceLocation()
        }
        val finish = dataStoreClient.getAllEntitiesOfType( "PieceLocation", PieceType.FINISH).map {
            it.toPieceLocation()
        }.firstOrNull()
        return finish?.let {
            boats.map { boat ->
                Player(
                    boat,
                    GeoDistance.distanceKm(boat.geoLocation, finish.geoLocation),
                    loadDistance(boat, PieceType.PIRATE),
                    loadDistance(boat, PieceType.SHARK)
                )
            }
        } ?: emptyList()
    }

    override fun loadDistance(pieceLocation: PieceLocation, pieceType: PieceType): List<Distance> =
        dataStoreClient.getAllEntitiesOfKind("PieceLocation").map {
            it.toPieceLocation()
        }.associateWith {
            GeoDistance.distanceKm(it.geoLocation, pieceLocation.geoLocation)
        }.map { Distance(it.key.name, it.value) }

    override fun getAllWaveData(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location> {
        val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        return mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
    }

    override fun getWaveDataOnly(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): String {
        val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction = siteListFunction, dataForSiteFunction)
        val withStored: MutableList<Location> = allWaveData
            .withStored(this)
        return withStored
            .toGoogleMapFormat()
    }

    override fun getAllWaveDataWithPieces(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location> {
        val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        val waveData: MutableList<Location> = mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
        return waveData.withStored(this).withStored(this)
            .withStored(this).withStored(this).withStored(this)
    }
}
