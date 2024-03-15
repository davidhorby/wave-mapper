package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.external.storage.DatastoreKind.PIECE_LOCATION_KIND
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toPieceLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.port.GameStorage

class StorageAdapter(private val dataStoreClient: Storable):GameStorage {
    override fun write(pieceLocation: PieceLocation) =
        dataStoreClient.writeToDatastore(kind = PIECE_LOCATION_KIND, pieceLocation = pieceLocation)

    override fun read(name: String): PieceLocation? = dataStoreClient
        .readFromDatastore(PIECE_LOCATION_KIND, name)
        ?.toPieceLocation()

    override fun getAllPieces(): List<PieceLocation> {
        return dataStoreClient.getAllEntitiesOfKind("PieceLocation").map { it.toPieceLocation() }
    }
}