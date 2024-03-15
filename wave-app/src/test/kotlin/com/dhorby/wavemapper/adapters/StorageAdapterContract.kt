package com.dhorby.wavemapper.adapters

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.google.cloud.datastore.Entity
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test

interface StorageAdapterContract {

    val dataStoreClient: Storable
    val pieceLocation:PieceLocation
    val multiplePieces:List<PieceLocation>
    val entityMatcher: Matcher<Entity>
    val pieceLocationMatcher: Matcher<PieceLocation>

    @Test
    fun `should store a piece`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.write(pieceLocation)
        val storedPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
    }

    @Test
    fun `should store multiple pieces`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        multiplePieces.forEach {
            storageAdapter.write(it)
        }
        val storedPieces = storageAdapter.getAllPieces()
        assertThat(storedPieces, present(hasSize(equalTo(2))))
    }

    @Test
    fun `should delete a datastore entity`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.write(pieceLocation)
        val storedPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.deleteEntity("PieceLocation", pieceLocation.id)
        val newStoredPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }

    @Test
    fun `should clear the datastore `(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.write(pieceLocation)
        val storedPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.clear("PieceLocation")
        val newStoredPieceLocation = storageAdapter.read(pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }
}