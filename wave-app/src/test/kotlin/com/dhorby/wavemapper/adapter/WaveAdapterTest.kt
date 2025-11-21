package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.wavemapper.external.google.GoogleMapsClientApi
import com.dhorby.wavemapper.external.metoffice.MetOfficeClient
import com.dhorby.wavemapper.model.GeoLocation
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.dhorby.wavemapper.storage.DataStoreClient
import com.dhorby.wavemapper.tracing.addRequestCount
import com.google.cloud.datastore.Datastore
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import org.http4k.client.ApacheClient
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.http4k.template.ViewModel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
class WaveAdapterTest {
//    companion object {
//        val emulator: DatastoreEmulatorContainer = DatastoreEmulatorContainer(
//            DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:441.0.0-emulators")
//        )
//        lateinit var datastore: Datastore
//        @JvmStatic
//        @BeforeAll
//        fun setup() {
//            emulator.start()
//            val options:DatastoreOptions = DatastoreOptions.newBuilder()
//                .setHost(emulator.emulatorEndpoint)
//                .setCredentials(NoCredentials.getInstance())
//                .setRetrySettings(ServiceOptions.getNoRetrySettings())
//                .setProjectId(emulator.getProjectId())
//                .build();
//            datastore = options.getService();
//        }
//
//        @JvmStatic
//        @AfterAll
//        fun tearDown() {
//            emulator.stop()
//        }
//
//    }

    private val events: (Event) -> Unit =
        EventFilters
            .AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(addRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    val pieceLocation =
        PieceLocation(
            id = "test",
            name = "test piece",
            geoLocation = GeoLocation(-0.34, 45.03),
            pieceType = PieceType.BOAT,
        )

    @Test
    fun `wave page should be present`(datastore: Datastore) {
        val storageAdapter = StorageAdapter(DataStoreClient(datastore))
        val waveAdapter =
            WaveAdapter(
                storageAdapter = storageAdapter,
                googleMapsClientApi = GoogleMapsClientApi(ApacheClient()),
                metOfficeClient = MetOfficeClient(),
            )
        storageAdapter.add(pieceLocation)
        val wavePage: ViewModel? = waveAdapter.getWavePage()
        assertThat(wavePage, present())
    }

    @Test
    fun `wave data should be present`(datastore: Datastore) {
        val storageAdapter = StorageAdapter(DataStoreClient(datastore))
        val waveAdapter =
            WaveAdapter(
                storageAdapter = storageAdapter,
                googleMapsClientApi = GoogleMapsClientApi(ApacheClient()),
                metOfficeClient = MetOfficeClient(),
            )
        storageAdapter.add(pieceLocation)
        val waveData: MutableList<Location> = waveAdapter.getWaveData()
        assertThat(waveData, present())
    }
}
