package com.dhorby.wavemapper.adapter

import com.dhorby.wavemapper.algorithms.GeoDistance
import com.dhorby.wavemapper.model.Distance
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.dhorby.wavemapper.model.Player
import com.dhorby.wavemapper.port.MetOfficePort
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.storage.EntityKind
import com.dhorby.wavemapper.storage.EntityKind.PIECE_LOCATION
import com.dhorby.wavemapper.storage.Storable
import com.dhorby.wavemapper.storage.toPieceLocation

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
