package com.dhorby.wavemapper.adapters

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toEntity
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import org.junit.jupiter.api.Test

interface StorageAdapterContract {

    val dataStoreClient: Storable
    val pieceLocation:PieceLocation

    @Test
    fun `should store a piece`() {
        val storageAdapter = StorageAdapter(dataStoreClient)
        val entity = storageAdapter.write(pieceLocation)
        assertThat(entity, present(equalTo(pieceLocation.toEntity())) )
    }
}