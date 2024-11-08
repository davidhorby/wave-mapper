package com.dhorby.wavemapper.external.google

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.http4k.client.ApacheClient
import kotlin.test.Test

class GoogleMapsClientTest {

    @Test
    fun `should retrieve the data`(){
        val locationData: String = GoogleMapsClient(ApacheClient()).getLocationData(23.00F, 34.02F)
        assertThat(locationData, containsSubstring("Al Shalateen Desert"))
    }

    @Test
    fun `should retrieve the data as object`(){
        val locationData = GoogleMapsClient(ApacheClient()).getLocationDatAsObject(23.00F, 34.02F)
        assertThat(locationData.plusCode.globalCode, equalTo("7GMP2229+2X5"))
    }

    @Test
    fun `should retrieve the data as object 2`(){
        val locationData = GoogleMapsClient(ApacheClient()).getLocationDatAsObject(23.00F, 34.02F)
        assertThat(locationData.results.get(1).addressComponents.get(0).longName, equalTo("Al Shalateen Desert"))
    }
}