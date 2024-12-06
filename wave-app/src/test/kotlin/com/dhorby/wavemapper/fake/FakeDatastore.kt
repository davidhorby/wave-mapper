package com.dhorby.wavemapper.fake

import com.dhorby.gcloud.external.junit.DataStoreExtension
import org.junit.jupiter.api.extension.RegisterExtension

class FakeDatastore {

    companion object {
        @JvmStatic
        @RegisterExtension
        val server = DataStoreExtension()
            .builder()
            .build()
    }

    private val datastore = server.localDatastoreHelper.options.service

    val dataStore = datastore
}