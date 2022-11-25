package com.dhorby.wavemapper.functional.functions

import com.dhorby.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.env.FunctionalTestEnv
import com.dhorby.wavemapper.env.TestData.testBoatLocation
import com.dhorby.wavemapper.env.TestData.testSharkLocation
import com.dhorby.wavemapper.env.siteId
import com.dhorby.wavemapper.env.siteName
import com.dhorby.wavemapper.getAllWaveData
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.WaveLocation
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class WaveDataFunctionsFunctionalTests: FunctionalTestEnv() {

    @Test
    fun `should be able to get all wave data`() {
        val allWaveData: MutableList<WaveLocation> = getAllWaveData(siteListFunctionFake, dataForSiteFunctionFake)
        assertThat(allWaveData.size, equalTo(1))
        val brittanyLightShip = allWaveData.first { it.id == siteId }
        assertThat(brittanyLightShip.name, equalTo(siteName))
    }

    @Test
    fun `should be able to get wave data for site`() {
        val siteData = dataForSiteFunctionFake(siteId)
        assertThat(siteData?.name, equalTo(siteName))
    }

    @Test
    fun `verify google format wave data`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[-13.34,45.0,'TestSite  2.1m 23km SSW', 'big']"
        val allWaveData: String = getAllWaveData(siteListFunctionFake, dataForSiteFunctionFake).toGoogleMapFormat()
        assertThat(allWaveData, equalTo(expectedResult))
    }

    @Test
    fun `verify google format wave data with missing wave data`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[-13.34,45.0,'TestSite  0.0m 0.0km 0.0', 'notavailable']"
        val allWaveData: String = getAllWaveData(siteListFunctionFake, dataForSiteWithNoDataFunctionFake).toGoogleMapFormat()
        assertThat(allWaveData, equalTo(expectedResult))
    }

    @Test
    fun `verify google format wave data with added shark`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[34.45,49.01,'BASKING [Sue]', 'shark']"
        val sharkData: String = listOf(testSharkLocation).toGoogleMapFormat()
        assertThat(sharkData, equalTo(expectedResult))
    }

    @Test
    fun `verify google format wave data with boat`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[39.45,-5.01,'SAIL [Albert]', 'boat']"
        val sharkData: String = listOf(testBoatLocation).toGoogleMapFormat()
        assertThat(sharkData, equalTo(expectedResult))
    }
}

