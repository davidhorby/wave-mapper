package com.dhorby.wavemapper.functional.functions

import com.dhorby.gcloud.data.TestData.testSharkLocation
import com.dhorby.wavemapper.getAllSharkLocationsFromDatastore
import com.dhorby.wavemapper.getSharkLocationsFromDatastore
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class DataStoreFunctionalTests {

    @Test
    fun `should be able to get shark location from data store`() {
        getSharkLocationsFromDatastore()?.let {
            assertThat(it, equalTo(testSharkLocation))
        }
    }

    @Test
    fun `should be able to get multiole shark locations from data store`() {
        getAllSharkLocationsFromDatastore().forEach {
            assertThat(it.pieceType, equalTo("SHARK"))
        }
    }
}