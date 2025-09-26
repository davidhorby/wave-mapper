package com.dhorby.wavemapper.external.google

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.http4k.client.ApacheClient
import kotlin.test.Test

class GoogleMapsClientTest {
    @Test
    fun `should retrieve the data`() {
        val locationData: String = GoogleMapsClientApi(ApacheClient()).getLocationData(23.00F, 34.02F)
        assertThat(locationData, containsSubstring("Al Shalateen Desert"))
    }

    @Test
    fun `should retrieve the data as object`() {
        val locationData = GoogleMapsClientApi(ApacheClient()).getLocationDatAsObject(23.00F, 34.02F)
        assertThat(locationData.plusCode.globalCode, equalTo("7GMP2229+2X5"))
    }

    @Test
    fun `should retrieve the long name`() {
        val locationData = GoogleMapsClientApi(ApacheClient()).getLocationDatAsObject(23.00F, 34.02F)
        assertThat(
            locationData.results
                .get(1)
                .addressComponents
                .get(0)
                .longName,
            equalTo("Al Shalateen Desert"),
        )
    }

    @Test
    fun `should retrieve the formatted address`() {
        val locationData = GoogleMapsClientApi(ApacheClient()).getLocationDatAsObject(23.00F, 34.02F)
        assertThat(locationData.results.get(1).formattedAddress, equalTo("Al Shalateen Desert, Red Sea Governorate 1910001, Egypt"))
    }
}
