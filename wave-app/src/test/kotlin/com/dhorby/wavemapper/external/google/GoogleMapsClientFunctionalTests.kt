package com.dhorby.wavemapper.external.google

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.http4k.client.ApacheClient
import org.junit.jupiter.api.Test

class GoogleMapsClientFunctionalTests {
    val fakeGoogleMapsHandler: FakeGoogleMapsHandler = FakeGoogleMapsHandler()

    @Test
    fun `should retrieve the map data `() {
        val locationData: String = GoogleMapsClientApi(ApacheClient()).getLocationData(23.00F, 34.02F)
        assertThat(locationData, containsSubstring("Al Shalateen Desert"))
    }
}
