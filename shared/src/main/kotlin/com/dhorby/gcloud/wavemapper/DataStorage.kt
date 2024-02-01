package com.dhorby.gcloud.wavemapper

import DataStoreClient
import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.model.*
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat


interface WaveDataActions {
    fun clear(kind: String)
    fun write(pieceLocation: PieceLocation)
    fun getKeysOfKind(pieceType: PieceType): List<PieceLocation>
    fun getAllLocations(pieceType: PieceType): List<PieceLocation>
    fun getAllWaveData(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location>

    fun getWaveDataOnly(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): String
    fun getAllWaveDataWithPieces(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location>

    fun loadDistance(pieceLocation: PieceLocation, pieceType: PieceType): List<Distance>
}

interface WaveDataCalculations {
    fun getDistances(): List<Player>
}

class DataStorage(private val dataStoreClient: DataStoreClient) : WaveDataActions, WaveDataCalculations {

    override fun clear(kind: String) {
        dataStoreClient.clearDatastore(kind)
    }

    override fun write(pieceLocation: PieceLocation) {
        this.dataStoreClient.writeToDatastore(pieceLocation)
    }

    override fun getKeysOfKind(pieceType: PieceType): List<PieceLocation> {
        return dataStoreClient.getPieces(pieceType).map {
            it.toPieceLocation()
        }
    }

    override fun getAllLocations(pieceType: PieceType): List<PieceLocation> = getKeysOfKind(pieceType)

    override fun getDistances(): List<Player> {
        val boats = dataStoreClient.getPieces( PieceType.BOAT).map {
            it.toPieceLocation()
        }
        val finish = dataStoreClient.getPieces( PieceType.FINISH).map {
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
        dataStoreClient.getPieces(pieceType).map {
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

    override fun getWaveDataOnly(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): String =
        getAllWaveData(siteListFunction = siteListFunction, dataForSiteFunction).withStored(PieceType.SHARK, this)
            .withStored(PieceType.BOAT, this).withStored(PieceType.PIRATE, this).withStored(PieceType.START, this)
            .withStored(PieceType.FINISH, this).toGoogleMapFormat()

    override fun getAllWaveDataWithPieces(
        siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location> {
        val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        val waveData: MutableList<Location> = mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
        return waveData.withStored(PieceType.SHARK, this).withStored(PieceType.BOAT, this)
            .withStored(PieceType.PIRATE, this).withStored(PieceType.START, this).withStored(PieceType.FINISH, this)
    }
}
