package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.wavemapper.game.testBoatLocation
import com.dhorby.wavemapper.port.StoragePort
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

interface RaceActionsContract {

    val fakeStorageAdapter: StoragePort
    val raceActions:RaceActions

    @Test
    fun addPiece() {
        val pieceLocation = testBoatLocation
        raceActions.addPiece(pieceLocation)
        assertThat(fakeStorageAdapter.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
    }

    @Test
    fun deletePiece() {
        val pieceLocation = testBoatLocation
        raceActions.addPiece(pieceLocation)
        assertThat(fakeStorageAdapter.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
        raceActions.deletePiece(pieceLocation)
        assertThat(fakeStorageAdapter.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), absent())
    }


}