package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.game.*
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.port.StoragePort

sealed class RaceActions1(raceActions: RaceActions)

class ResetRace(private val raceActions: RaceActions): RaceActions1(raceActions) {
    operator fun invoke() = raceActions.resetRace()
}

class RaceActions(private val storagePort: StoragePort) {

    fun startRace() {
        storagePort.write(WaveHandlers.start)
        storagePort.write(WaveHandlers.finish)
        storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
            .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
            .forEach(storagePort::write)
    }

    fun clear() {
        storagePort.clear(EntityKind.PIECE_LOCATION)
    }

    fun resetRace() {
        storagePort.write(startLocation)
        storagePort.write(finishLocation)
        storagePort.write(testSharkLocation)
        storagePort.write(testBoatLocation)
        storagePort.write(testPirateLocation)
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