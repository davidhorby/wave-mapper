package com.dhorby.wavemapper

import com.dhorby.wavemapper.TestData.testLocation
import com.dhorby.wavemapper.TestData.testSiteLocation
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import org.junit.jupiter.api.Test

internal class WaveDataFunctionsFunctionalTests {

    val dataForSiteFunctionFake:DataForSiteFunction = {
        testLocation
    }

    val siteListFunctionFake:SiteListFunction = {
        listOf(testSiteLocation)
    }

    @Test
    fun `should be able to get all wave data`() {
        val allWaveData: List<Location> = getAllWaveData(siteListFunctionFake, dataForSiteFunctionFake)
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
    fun `get list of sites`() {
        println(Constants.siteListUrl)
        val sites = siteListFunction()
        println(sites)
    }
}
