package com.dhorby.wavemapper.fake

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.Player
import com.dhorby.wavemapper.port.StoragePort

class FakeStorageAdapter: StoragePort {
    override fun write(pieceLocation: PieceLocation) {
        TODO("Not yet implemented")
    }

    override fun read(name: String): PieceLocation? {
        TODO("Not yet implemented")
    }

    override fun getAllPieces(): List<PieceLocation> {
        TODO("Not yet implemented")
    }

    override fun getKeysOfType(kind: EntityKind, pieceType: PieceType): List<PieceLocation> {
        TODO("Not yet implemented")
    }

    override fun getLocationData(): List<Location> {
        TODO("Not yet implemented")
    }

    override fun getDistances(): List<Player> {
        TODO("Not yet implemented")
    }

    override fun clear(kind: EntityKind) {
        TODO("Not yet implemented")
    }

    override fun addPiece(pieceLocation: PieceLocation) {
        TODO("Not yet implemented")
    }
}