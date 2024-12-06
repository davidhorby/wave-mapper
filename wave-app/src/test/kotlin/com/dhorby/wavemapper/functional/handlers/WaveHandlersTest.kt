package com.dhorby.wavemapper.functional.handlers

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.wavemapper.env.FunctionalTestEnv
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
internal class WaveHandlersTest : FunctionalTestEnv() {

//    private val events: (Event) -> Unit =
//        EventFilters.AddTimestamp()
//            .then(EventFilters.AddEventName())
//            .then(EventFilters.AddZipkinTraces())
//            .then(AddRequestCount())
//            .then(AutoMarshallingEvents(Jackson))
//
//    @Test
//    @Disabled
//    fun `returns OK with invalid site list`(datastore: Datastore) {
//        val invalidSiteList = { emptyList<Site>().toMutableList() }
//        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
//        val storageAdapter = StorageAdapter(dataStoreClient)
//        val wavePage: (Request) -> Response = WaveHandlers(
//            wavePort = WaveAdapter(
//                siteListFunction = invalidSiteList,
//                metOfficeClient = MetOfficeClient(),
//                dataForSiteFunction = dataForSiteFunction,
//                storageAdapter = storageAdapter,
//                googleMapsClient = GoogleMapsClient(ApacheClient())
//            )
//        ).getWaveData()
//        val response = wavePage(Request(Method.GET, "/"))
//        assertThat(response.status, equalTo(OK))
//        assertThat(response.bodyString(), equalTo("[]"))
//    }
}
