package com.dhorby.wavemapper.adapters

import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.fake.FakeDataStoreClient

class StorageAdapterFunctionalTests:StorageAdapterContract {
    override val dataStoreClient: Storable = FakeDataStoreClient()
    override val pieceLocation = PieceLocation(
        id = "test",
        name = "test piece",
        geoLocation = GeoLocation(-0.34, 45.03),
        pieceType = PieceType.BOAT
    )
    init {
        dataStoreClient.writeToDatastore(pieceLocation)
    }
}