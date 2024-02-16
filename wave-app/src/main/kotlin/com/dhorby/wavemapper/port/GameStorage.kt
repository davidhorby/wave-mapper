package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity

interface GameStorage {
    fun write(pieceLocation: PieceLocation): Entity?
}