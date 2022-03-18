package com.dhorby.wavemapper.functional.handlers

import com.dhorby.wavemapper.TestData.testLocation
import com.dhorby.wavemapper.env.FunctionalTestEnv
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site
import com.dhorby.wavemapper.model.locationListBodyLens
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test

internal class WaveHandlersTest: FunctionalTestEnv() {

    @Test
    fun `returns OK with site list`() {
        val wavePage: (Request) -> Response = WaveHandlers(siteListFunction = siteListFunctionFake, dataForSiteFunction = dataForSiteFunctionFake).getWaveData()
        val response = wavePage(Request(Method.GET, "/"))
        assertThat(response.status, equalTo(OK))
        val location: List<Location> = locationListBodyLens.extract(response)
        assertThat(location, equalTo(listOf(testLocation)))
    }

    @Test
    fun `returns OK with invalid site list`() {
        val invalidSiteList = { emptyList<Site>() }
        val wavePage: (Request) -> Response = WaveHandlers(siteListFunction = invalidSiteList, dataForSiteFunction = dataForSiteFunctionFake).getWaveData()
        val response = wavePage(Request(Method.GET, "/"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("[]"))
    }
}
