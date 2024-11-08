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
        assertThat(locationData.plus_code.global_code, equalTo("7GMP2229+2X5"))
    }
}