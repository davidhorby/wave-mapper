package com.dhorby.wavemapper.adapter

import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.storage.DataStoreClient
import com.dhorby.wavemapper.storage.EntityKind
import com.dhorby.wavemapper.storage.EntityKind.PIECE_LOCATION
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.present
import org.http4k.events.AutoMarshallingEvents.invoke
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(com.dhorby.gcloud.external.junit.DataStoreExtension::class)
interface StorageAdapterContract {
    val pieceLocation: PieceLocation
    val multiplePieces: List<PieceLocation>
    val entityMatcher: Matcher<Entity>
    val pieceLocationMatcher: Matcher<PieceLocation>

    @Test
    fun `should store a piece`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION, pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
    }

    @Test
    fun `should store multiple pieces`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        multiplePieces.forEach {
            storageAdapter.add(it)
        }
        val storedPieces = storageAdapter.getAllPieces()
        assertThat(storedPieces, present(hasSize(equalTo(2))))
    }

    @Test
    fun `should delete a datastore entity`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION, pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.delete(PIECE_LOCATION, pieceLocation.id)
        val newStoredPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION, pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }

    @Test
    fun `should clear the datastore `(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        storageAdapter.add(pieceLocation)
        val storedPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION, pieceLocation.id)
        assertThat(storedPieceLocation, present(pieceLocationMatcher))
        storageAdapter.clear(PIECE_LOCATION)
        val newStoredPieceLocation = storageAdapter.getPiece(EntityKind.PIECE_LOCATION, pieceLocation.id)
        assertThat(newStoredPieceLocation, absent())
    }
}
