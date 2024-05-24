package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.game.*
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.format.Gson.asJsonObject

class RaceActions(private val storagePort: StoragePort) {

    fun startRace():String {
        val players = storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
        return if (players.isEmpty()) "Not enough players"
        else {
            storagePort.add(WaveHandlers.start)
            storagePort.add(WaveHandlers.finish)
            players
                .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
                .forEach(storagePort::add)
            "Success"
        }
    }

    fun clear(): String {
        storagePort.clear(EntityKind.PIECE_LOCATION)
        return "Success"
    }

    fun resetRace(): String {
        storagePort.add(startLocation)
        storagePort.add(finishLocation)
        storagePort.add(testSharkLocation)
        storagePort.add(testBoatLocation)
        storagePort.add(testPirateLocation)
        return "Success"
    }

    fun move(): String {
        storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storagePort::add)
        return  storagePort.getLocationData().toGoogleMapFormatList().asJsonObject().toString()
    }

    fun addPiece(pieceLocation: PieceLocation) {
        storagePort.add(pieceLocation)
    }

    fun deletePiece(pieceLocation: PieceLocation) {
        storagePort.delete(EntityKind.PIECE_LOCATION, pieceLocation.id)
    }

    fun getPiece(kind: EntityKind, keyValue:String): PieceLocation? {
        return storagePort.getPiece(kind, keyValue)
    }

    fun getStartAndFinish(): List<PieceLocation> {
        return storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START) + storagePort.getKeysOfType(
            EntityKind.PIECE_LOCATION,
            PieceType.FINISH
        )
    }
}