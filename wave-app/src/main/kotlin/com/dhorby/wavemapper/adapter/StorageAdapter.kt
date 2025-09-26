package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.algorithms.GeoDistance
import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toPieceLocation
import com.dhorby.gcloud.model.*
import com.dhorby.wavemapper.port.MetOfficePort
import com.dhorby.wavemapper.port.StoragePort

class StorageAdapter(
    private val dataStoreClient: Storable,
) : MetOfficePort(),
    StoragePort {
    override fun getAllPieces(): List<PieceLocation> = dataStoreClient.getAllEntitiesOfKind(PIECE_LOCATION).map { it.toPieceLocation() }

    override fun getLocationData(): List<Location> = waveLocations() + getAllPieces()

    override fun getDistances(): List<Player> {
        val boats =
            dataStoreClient.getAllEntitiesOfType(PIECE_LOCATION, PieceType.BOAT).map {
                it.toPieceLocation()
            }
        val finish =
            dataStoreClient
                .getAllEntitiesOfType(PIECE_LOCATION, PieceType.FINISH)
                .map {
                    it.toPieceLocation()
                }.firstOrNull()
        return finish?.let {
            boats.map { boat ->
                Player(
                    boat,
                    GeoDistance.distanceKm(boat.geoLocation, finish.geoLocation),
                    loadDistance(boat),
                )
            }
        } ?: emptyList()
    }

    override fun add(pieceLocation: PieceLocation) {
        dataStoreClient.add(kind = PIECE_LOCATION, pieceLocation = pieceLocation)
    }

    override fun delete(
        kind: EntityKind,
        key: String,
    ) {
        dataStoreClient.deleteEntity(kind, key)
    }

    override fun getPiece(
        kind: EntityKind,
        key: String,
    ): PieceLocation? = dataStoreClient.getEntity(kind, key)?.toPieceLocation()

    override fun clear(kind: EntityKind) {
        dataStoreClient.clearDatastore(kind)
    }

    override fun getKeysOfType(
        kind: EntityKind,
        pieceType: PieceType,
    ): List<PieceLocation> =
        dataStoreClient.getAllEntitiesOfType(kind, pieceType).map {
            it.toPieceLocation()
        }

    private fun loadDistance(pieceLocation: PieceLocation): List<Distance> =
        dataStoreClient
            .getAllEntitiesOfKind(PIECE_LOCATION)
            .map {
                it.toPieceLocation()
            }.associateWith {
                GeoDistance.distanceKm(it.geoLocation, pieceLocation.geoLocation)
            }.map { Distance(it.key.name, it.value) }
}
