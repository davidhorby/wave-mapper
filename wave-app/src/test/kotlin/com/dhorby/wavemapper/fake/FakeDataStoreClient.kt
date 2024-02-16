package com.dhorby.wavemapper.fake

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toEntity
import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity

class FakeDataStoreClient: Storable {
    private val storedEntities = mutableListOf(String, PieceLocation)
    override fun writeToDatastore(pieceLocation: PieceLocation): Entity? {
        storedEntities.add(pieceLocation)
        return pieceLocation.toEntity()
    }
}