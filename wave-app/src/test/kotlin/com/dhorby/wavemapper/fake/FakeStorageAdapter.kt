package com.dhorby.wavemapper.fake

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.Player
import com.dhorby.wavemapper.port.StoragePort

class FakeStorageAdapter: StoragePort {

    private val pieces = mutableMapOf<String, PieceLocation>()

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

    override fun add(pieceLocation: PieceLocation) {
        pieces[pieceLocation.id] = pieceLocation
    }

    override fun getPiece(kind: EntityKind, key: String): PieceLocation? {
        TODO("Not yet implemented")
    }

    override fun delete(kind: EntityKind, key: String) {
        pieces.remove(key)
    }


    fun get(key:String): PieceLocation? = pieces[key]
}