package com.dhorby.wavemapper.fake

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toEntity
import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity

class FakeDataStoreClient: Storable {
    private val storedEntities = mutableMapOf<String, Map<String, PieceLocation>>()

    override fun writeToDatastore(kind:String, pieceLocation: PieceLocation): Entity {
        storedEntities[kind] = mapOf(pieceLocation.id to pieceLocation)
        return pieceLocation.toEntity()
    }

    override fun readFromDatastore(kind: String, name: String): Entity? {
        return storedEntities[kind]?.get(name)?.toEntity()
    }
}