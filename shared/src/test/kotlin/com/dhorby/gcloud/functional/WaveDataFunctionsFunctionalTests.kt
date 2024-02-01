package com.dhorby.gcloud.functional


import com.dhorby.gcloud.data.TestData
import com.dhorby.gcloud.data.TestData.siteId
import com.dhorby.gcloud.data.TestData.siteName
import com.dhorby.gcloud.data.TestData.testBoatLocation
import com.dhorby.gcloud.data.TestData.testPirateLocation
import com.dhorby.gcloud.data.TestData.testSharkLocation
import com.dhorby.gcloud.env.FunctionalTestEnv
import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.gcloud.wavemapper.getAllWaveData
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class WaveDataFunctionsFunctionalTests: FunctionalTestEnv() {

    @Test
    fun `should be able to get all wave data`() {
        val allWaveData: MutableList<Location> = getAllWaveData(siteListFunctionFake, dataForSiteFunctionFake)
        assertThat(allWaveData.size, equalTo(1))
        val brittanyLightShip = allWaveData.first { it.id == siteId }
        assertThat(brittanyLightShip.name, equalTo(TestData.siteName))
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
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[34.45,49.01,'[Sue]', 'shark']"
        val sharkData: String = listOf(testSharkLocation).toGoogleMapFormat()
        assertThat(sharkData, equalTo(expectedResult))
    }

    @Test
    fun `verify google format wave data with boat`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[39.45,-5.01,'[Albert]', 'boat']"
        val sharkData: String = listOf(testBoatLocation).toGoogleMapFormat()
        assertThat(sharkData, equalTo(expectedResult))
    }

    @Test
    fun `verify google format wave data with pirate`() {
        val expectedResult = "['Lat', 'Long', 'Name', 'Marker'],[60.45,-15.01,'[Captain Morgan]', 'pirate']"
        val sharkData: String = listOf(testPirateLocation).toGoogleMapFormat()
        assertThat(sharkData, equalTo(expectedResult))
    }
}

