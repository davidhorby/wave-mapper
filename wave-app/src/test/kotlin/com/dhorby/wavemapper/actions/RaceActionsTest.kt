package com.dhorby.wavemapper.actions

import com.dhorby.wavemapper.fake.FakeStorageAdapter
import com.dhorby.wavemapper.game.testBoatLocation
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class RaceActionsTest {

    val fakeStorageAdapter = FakeStorageAdapter()
    val raceActions = RaceActions(fakeStorageAdapter)

    @Test
    fun addPiece() {
        val pieceLocation = testBoatLocation
        raceActions.addPiece(pieceLocation)
        assertThat(fakeStorageAdapter.get(key = pieceLocation.id), equalTo(testBoatLocation))
    }
}