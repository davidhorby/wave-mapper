package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.external.storage.DatastoreKind.PIECE_LOCATION_KIND
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toPieceLocation
import com.dhorby.gcloud.model.*
import com.dhorby.wavemapper.port.DatastorePort
import com.dhorby.wavemapper.port.MetOfficePort
import org.http4k.core.Response
import org.http4k.core.Status

class StorageAdapter(private val dataStoreClient: Storable):DatastorePort, MetOfficePort() {
    override fun write(pieceLocation: PieceLocation) =
        dataStoreClient.writeToDatastore(kind = PIECE_LOCATION_KIND, pieceLocation = pieceLocation)

    override fun read(name: String): PieceLocation? = dataStoreClient
        .readFromDatastore(PIECE_LOCATION_KIND, name)
        ?.toPieceLocation()

    override fun getAllPieces(): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfKind("PieceLocation").map { it.toPieceLocation() }
    }

    fun getTheData(): List<Location> {
        val storedPieces: List<PieceLocation> = getAllPieces()
        val waveLocations: MutableList<Location> = waveLocations()
        return   waveLocations+storedPieces
    }

    fun getDistances(): List<Player> {
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
                    loadDistance(boat)
                )
            }
        } ?: emptyList()
    }

    fun addPiece(pieceLocation: PieceLocation)  {
        write(pieceLocation)
        Response(Status.FOUND).header("Location", "/")
    }

    fun clear(kind: String) {
        dataStoreClient.clearDatastore(kind)
    }


    fun getKeysOfType(kind: String, pieceType: PieceType): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfType(kind, pieceType).map {
            it.toPieceLocation()
        }
    }

    private fun loadDistance(pieceLocation: PieceLocation): List<Distance> =
        dataStoreClient.getAllEntitiesOfKind("PieceLocation").map {
            it.toPieceLocation()
        }.associateWith {
            GeoDistance.distanceKm(it.geoLocation, pieceLocation.geoLocation)
        }.map { Distance(it.key.name, it.value) }
}