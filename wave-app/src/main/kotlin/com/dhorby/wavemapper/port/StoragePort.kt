package com.dhorby.wavemapper.port

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.Player

interface StoragePort {
    fun write(pieceLocation: PieceLocation)
    fun read(name: String): PieceLocation?
    fun getAllPieces():List<PieceLocation>
    fun getKeysOfType(kind: EntityKind, pieceType: PieceType): List<PieceLocation>
    fun getLocationData(): List<Location>
    fun getDistances(): List<Player>
    fun clear(kind: EntityKind)
}