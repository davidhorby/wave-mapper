package com.dhorby.wavemapper.port

import com.dhorby.gcloud.external.storage.EntityKind
import model.Location
import model.PieceLocation
import model.PieceType
import model.Player

interface StoragePort {
    fun getAllPieces(): List<PieceLocation>

    fun getKeysOfType(
        kind: EntityKind,
        pieceType: PieceType,
    ): List<PieceLocation>

    fun getLocationData(): List<Location>

    fun getDistances(): List<Player>

    fun clear(kind: EntityKind)

    fun add(pieceLocation: PieceLocation)

    fun getPiece(
        kind: EntityKind,
        key: String,
    ): PieceLocation?

    fun delete(
        kind: EntityKind,
        key: String,
    )
}
