package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity

interface Storable {
    fun readFromDatastore(kind: String, name: String): Entity?
    fun writeToDatastore(kind: String, pieceLocation: PieceLocation): Entity?
}