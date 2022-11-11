package com.dhorby.wavemapper.functional.functions

import com.dhorby.wavemapper.env.FunctionalTestEnv
import com.dhorby.wavemapper.env.siteId
import com.dhorby.wavemapper.env.siteName
import com.dhorby.wavemapper.getAllWaveData
import com.dhorby.wavemapper.model.Location
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

internal class WaveDataFunctionsFunctionalTests: FunctionalTestEnv() {

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
}
