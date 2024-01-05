package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.PieceLocation

interface GameStorage {
    fun write(pieceLocation: PieceLocation)
}