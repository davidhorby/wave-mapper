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
        storagePort.write(WaveHandlers.start)
        storagePort.write(WaveHandlers.finish)
        storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
            .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
            .forEach(storagePort::write)
        return "Success"
    }

    fun clear(): String {
        storagePort.clear(EntityKind.PIECE_LOCATION)
        return "Success"
    }

    fun resetRace(): String {
        storagePort.write(startLocation)
        storagePort.write(finishLocation)
        storagePort.write(testSharkLocation)
        storagePort.write(testBoatLocation)
        storagePort.write(testPirateLocation)
        return "Success"
    }

    fun move(): String {
        storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
            .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
            .forEach(storagePort::write)
        return  storagePort.getLocationData().toGoogleMapFormatList().asJsonObject().toString()
    }

    fun addPiece(pieceLocation: PieceLocation) {
        storagePort.addPiece(pieceLocation)
    }

    fun getStartAndFinish(): List<PieceLocation> {
        return storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START) + storagePort.getKeysOfType(
            EntityKind.PIECE_LOCATION,
            PieceType.FINISH
        )
    }
}