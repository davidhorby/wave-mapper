package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.external.storage.DatastoreKind.PIECE_LOCATION_KIND
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.port.GameStorage
import com.google.cloud.datastore.Entity

class StorageAdapter(private val dataStoreClient: Storable):GameStorage {
    override fun write(pieceLocation: PieceLocation): Entity? {
        return dataStoreClient.writeToDatastore(PIECE_LOCATION_KIND, pieceLocation)
    }
}