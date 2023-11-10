package com.dhorby.gcloud.functional.functions

import DataStoreClient
import com.dhorby.gcloud.data.TestData
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.Player
import com.dhorby.gcloud.wavemapper.DataStorage
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DataStoreFunctionalTests {

    val dataStorage = DataStorage(DataStoreClient())


    @Test
    fun `can calculate the distance from pirates`() {
        val player: Player = Player(pieceLocation = TestData.testBoatLocation, distanceToFinish = 0)
        val distanceFromPirates = dataStorage.loadDistanceFromPirates(TestData.testBoatLocation)
    }

    @Test
    @Disabled
    fun `should be able to get multiple shark locations from data store`() {
        val allSharkLocationsFromDatastore = dataStorage.getAllLocations(PieceType.SHARK)
        assertThat(allSharkLocationsFromDatastore.size, greaterThan(0))
        allSharkLocationsFromDatastore.forEach {
            assertThat(it.pieceType.name, equalTo("SHARK"))
        }
    }
}