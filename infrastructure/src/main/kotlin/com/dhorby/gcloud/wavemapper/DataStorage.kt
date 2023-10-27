package com.dhorby.gcloud.wavemapper

import DataStoreClient
import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.model.*
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat

class DataStorage(private val dataStoreClient: DataStoreClient) {

    fun write(pieceLocation: PieceLocation) {
        this.dataStoreClient.writeToDatastore(pieceLocation)
    }

    fun getSharkLocationsFromDatastore(): PieceLocation? {
        return dataStoreClient.readFromDatastore()?.toPieceLocation()

    }

    fun getKeysOfKind(pieceType:PieceType) : List<PieceLocation> {
        return dataStoreClient.getKeysOfKind("PieceLocation", pieceType).map {
            it.toPieceLocation()
        }
    }

    fun clear(kind:String) {
        dataStoreClient.clearDatastore(kind)
    }

    fun getAllLocationsFromDatastore(pieceType: PieceType): List<PieceLocation> =
        getKeysOfKind( pieceType)

    fun getDistances(): List<Player> {
        val boats = dataStoreClient.getKeysOfKind("PieceLocation", PieceType.BOAT).map {
            it.toPieceLocation()
        }
        val finish = dataStoreClient.getKeysOfKind("PieceLocation", PieceType.FINISH).map {
            it.toPieceLocation()
        }.firstOrNull()
        return finish?.let {
            boats.map { boat ->
                Player(
                    boat,
                    GeoDistance.distanceKm(boat.geoLocation, finish.geoLocation)
                )
            }
        } ?: emptyList()
    }

    fun loadDistanceFromPirates(player: Player): Map<PieceLocation, Int> =
        dataStoreClient.getKeysOfKind("PieceLocation", PieceType.PIRATE).map {
            it.toPieceLocation()
        }.associateWith {
            GeoDistance.distanceKm(it.geoLocation, player.pieceLocation.geoLocation)
        }

    fun getAllWaveData(
        siteListFunction: SiteListFunction,
        dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location> {
        val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        return mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
    }

    fun getWaveDataOnly(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): String {
        val waveData: String =
            getAllWaveData(siteListFunction = siteListFunction, dataForSiteFunction)
                .withStored(PieceType.SHARK, this)
                .withStored(PieceType.BOAT, this)
                .withStored(PieceType.PIRATE, this)
                .withStored(PieceType.START, this)
                .withStored(PieceType.FINISH, this)
                .toGoogleMapFormat()
        return waveData
    }



    fun getAllWaveDataWithPieces(
        siteListFunction: SiteListFunction,
        dataForSiteFunction: DataForSiteFunction
    ): MutableList<Location> {
        val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        val waveData: MutableList<Location> = mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
        return waveData.withStored(PieceType.SHARK, this)
            .withStored(PieceType.BOAT, this)
            .withStored(PieceType.PIRATE, this)
            .withStored(PieceType.START, this)
            .withStored(PieceType.FINISH, this)
    }
}
