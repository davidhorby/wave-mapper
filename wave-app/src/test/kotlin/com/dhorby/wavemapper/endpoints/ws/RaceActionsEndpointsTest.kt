package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.fake.FakeStorageAdapter
import org.junit.jupiter.api.Test

class RaceActionsEndpointsTest {

    val fakeStorageAdapter = FakeStorageAdapter()
    val raceActions = RaceActions(fakeStorageAdapter)

    @Test
    fun start() {

        RaceActionsEndpoints.Start(raceActions)
//        assertThat(fakeStorageAdapter)
    }

    @Test
    fun clear() {
    }

    @Test
    fun reset() {
    }

    @Test
    fun move() {
    }
}