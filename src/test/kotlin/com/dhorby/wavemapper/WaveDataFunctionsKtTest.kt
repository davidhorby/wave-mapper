package com.dhorby.wavemapper

import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.secrets.AccessSecretVersion
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import org.junit.jupiter.api.Test

internal class WaveDataFunctionsKtTest {

    private val mapsApiKey: String =
        AccessSecretVersion.accessSecretVersion("mapsApiKey") ?: throw Exception("Invalid Maps API key")
    private val metOfficeApiKey: String =
        AccessSecretVersion.accessSecretVersion("MetOfficeApiKey") ?: throw Exception("Invalid Met Office API key")

    @Test
    fun `should be able to get all wave data`() {
        val allWaveData: List<Location> = getAllWaveData(siteListFunction)
        assertThat(allWaveData.size, greaterThan(11))
        val brittanyLightShip = allWaveData.filter { it.id == "162163" }.first()
        assertThat(brittanyLightShip.name, equalTo("BRITTANY"))
    }

    @Test
    fun `should be able to get wave data for site`() {
        val siteData = getWaveDataForSite("162163")
        assertThat(siteData?.name, equalTo("BRITTANY"))
    }

    @Test
    fun `get list of sites`() {
        println(Constants.siteListUrl)
        val sites = siteListFunction()
        println(sites)
    }
}
