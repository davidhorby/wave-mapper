package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toPieceLocation
import com.dhorby.gcloud.model.*
import com.dhorby.wavemapper.port.MetOfficePort
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.core.Response
import org.http4k.core.Status

class StorageAdapter(private val dataStoreClient: Storable):StoragePort, MetOfficePort() {
    override fun write(pieceLocation: PieceLocation) =
        dataStoreClient.writeToDatastore(kind = PIECE_LOCATION, pieceLocation = pieceLocation)

    override fun read(name: String): PieceLocation? = dataStoreClient
        .readFromDatastore(PIECE_LOCATION, name)
        ?.toPieceLocation()

    override fun getAllPieces(): List<PieceLocation> =
        dataStoreClient.getAllEntitiesOfKind(PIECE_LOCATION).map { it.toPieceLocation() }

    override fun getLocationData(): List<Location> = waveLocations() + getAllPieces()

    override fun getDistances(): List<Player> {
        val boats = dataStoreClient.getAllEntitiesOfType( PIECE_LOCATION, PieceType.BOAT).map {
            it.toPieceLocation()
        }
        val finish = dataStoreClient.getAllEntitiesOfType( PIECE_LOCATION, PieceType.FINISH).map {
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

    override fun clear(kind: EntityKind) {
        dataStoreClient.clearDatastore(kind)
    }

    fun deleteEntity(kind: EntityKind, id:String) {
        dataStoreClient.deleteEntity(kind, id)
    }

    override fun getKeysOfType(kind: EntityKind, pieceType: PieceType): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfType(kind, pieceType).map {
            it.toPieceLocation()
        }
    }

    private fun loadDistance(pieceLocation: PieceLocation): List<Distance> =
        dataStoreClient.getAllEntitiesOfKind(PIECE_LOCATION).map {
            it.toPieceLocation()
        }.associateWith {
            GeoDistance.distanceKm(it.geoLocation, pieceLocation.geoLocation)
        }.map { Distance(it.key.name, it.value) }
}