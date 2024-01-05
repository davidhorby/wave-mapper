package com.dhorby.wavemapper.adapter

import DataStoreClient
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.port.GameStorage

class DatastoreGameStorage(private val dataStoreClient: DataStoreClient):GameStorage {
    override fun write(pieceLocation: PieceLocation) {
        TODO("Not yet implemented")
    }
}