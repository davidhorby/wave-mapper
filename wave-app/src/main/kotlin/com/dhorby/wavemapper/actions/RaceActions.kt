package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.adapter.WaveAdapter
import com.dhorby.wavemapper.game.*
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.format.Gson.asJsonObject

@Suppress("SameReturnValue")
class RaceActions(private val storagePort: StoragePort) {

    fun startRace(): String {
        val players = storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
        return if (players.isEmpty()) {
            "Not enough players"
        } else {
            storagePort.add(WaveAdapter.start)
            storagePort.add(WaveAdapter.finish)
            players
                .map { it.copy(geoLocation = WaveAdapter.start.geoLocation) }
                .forEach(storagePort::add)
            "Success"
        }
    }

    fun clear(): String {
        storagePort.clear(EntityKind.PIECE_LOCATION)
        return "Success"
    }

    fun resetRace() {
        storagePort.add(startLocation)
        storagePort.add(finishLocation)
        storagePort.add(testSharkLocation)
        storagePort.add(testBoatLocation)
        storagePort.add(testPirateLocation)
    }

    fun move(): String {
        storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storagePort::add)
        return storagePort.getLocationData().toGoogleMapFormatList().asJsonObject().toString()
    }

    fun addPiece(pieceLocation: PieceLocation) {
        storagePort.add(pieceLocation)
    }

    fun deletePiece(pieceLocation: PieceLocation) {
        storagePort.delete(EntityKind.PIECE_LOCATION, pieceLocation.id)
    }

// --Commented out by Inspection START (20/12/2024, 12:37):
//    fun getPiece(kind: EntityKind, keyValue: String): PieceLocation? {
//        return storagePort.getPiece(kind, keyValue)
//    }
// --Commented out by Inspection STOP (20/12/2024, 12:37)

    fun getStartAndFinish(): List<PieceLocation> {
        return storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START) + storagePort.getKeysOfType(
            EntityKind.PIECE_LOCATION,
            PieceType.FINISH
        )
    }
}