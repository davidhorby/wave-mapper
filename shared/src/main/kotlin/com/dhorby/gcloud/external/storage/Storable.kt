package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity

interface Storable {
    fun writeToDatastore(pieceLocation: PieceLocation): Entity?
}