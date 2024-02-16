package com.dhorby.wavemapper.adapters

import com.dhorby.gcloud.external.storage.DatastoreKind.PIECE_LOCATION_KIND
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.external.storage.toEntity
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.fake.FakeDataStoreClient
import com.google.cloud.datastore.Entity
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo

class StorageAdapterFunctionalTests:StorageAdapterContract {
    override val dataStoreClient: Storable = FakeDataStoreClient()
    override val pieceLocation = PieceLocation(
        id = "test",
        name = "test piece",
        geoLocation = GeoLocation(-0.34, 45.03),
        pieceType = PieceType.BOAT
    )
    override val entityMatcher: Matcher<Entity> = equalTo(pieceLocation.toEntity())

    init {
        dataStoreClient.writeToDatastore(PIECE_LOCATION_KIND,pieceLocation)
    }
}