package com.dhorby.wavemapper.functional.handlers

import com.dhorby.gcloud.model.Site
import com.dhorby.wavemapper.env.FunctionalTestEnv
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test

internal class WaveHandlersTest: FunctionalTestEnv() {

    @Test
    fun `returns OK with invalid site list`() {
        val invalidSiteList = { emptyList<Site>().toMutableList() }
        val wavePage: (Request) -> Response = WaveHandlers(siteListFunction = invalidSiteList, dataForSiteFunction = dataForSiteFunctionFake).getWaveData()
        val response = wavePage(Request(Method.GET, "/"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("[]"))
    }
}
