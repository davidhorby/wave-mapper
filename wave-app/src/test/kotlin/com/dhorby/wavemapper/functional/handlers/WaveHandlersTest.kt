package com.dhorby.wavemapper.functional.handlers

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.model.Site
import com.dhorby.wavemapper.AddRequestCount
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.env.FunctionalTestEnv
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.google.cloud.datastore.Datastore
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
internal class WaveHandlersTest : FunctionalTestEnv() {

    private val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    @Test
    fun `returns OK with invalid site list`(datastore: Datastore) {
        val invalidSiteList = { emptyList<Site>().toMutableList() }
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val wavePage: (Request) -> Response = WaveHandlers(
            siteListFunction = invalidSiteList,
            dataForSiteFunction = dataForSiteFunctionFake,
            storageAdapter = StorageAdapter(dataStoreClient)
        ).getWaveData()
        val response = wavePage(Request(Method.GET, "/"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("[]"))
    }
}
