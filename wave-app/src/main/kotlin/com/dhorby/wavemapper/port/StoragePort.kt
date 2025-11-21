package com.dhorby.wavemapper.port

import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.dhorby.wavemapper.model.Player
import com.dhorby.wavemapper.storage.EntityKind

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
