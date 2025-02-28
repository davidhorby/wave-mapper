package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.PieceLocation
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
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION,pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
    }

    @Test
    fun `should store multiple pieces`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        multiplePieces.forEach {
            storageAdapter.add(it)
        }
        val storedPieces = storageAdapter.getAllPieces()
        assertThat(storedPieces, present(hasSize(equalTo(2))))
    }

    @Test
    fun `should delete a datastore entity`(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION,pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.delete(PIECE_LOCATION, pieceLocation.id)
        val newStoredPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION,pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }

    @Test
    fun `should clear the datastore `(){
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION,pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.clear(PIECE_LOCATION)
        val newStoredPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION,pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }
}