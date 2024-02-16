package com.dhorby.wavemapper.adapters

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.google.cloud.datastore.Entity
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import org.junit.jupiter.api.Test

interface StorageAdapterContract {

    val dataStoreClient: Storable
    val pieceLocation:PieceLocation
    val entityMatcher: Matcher<Entity>
    val pieceLocationMatcher: Matcher<PieceLocation>

    @Test
    fun `should store a piece`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        val entity: Entity? = storageAdapter.write(pieceLocation)
        assertThat(entity, present(entityMatcher))
        val storedPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
    }
}