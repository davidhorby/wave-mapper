package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.LatLng


fun PieceLocation.toEntity(): Entity {
    val key = Key.newBuilder("test", "PieceLocation", "${this.id}").build()
    val pieceLocationEntity: com.google.cloud.datastore.Entity = com.google.cloud.datastore.Entity
        .newBuilder(key)
        .set("id", this.id)
        .set("name", this.name)
        .set("location", LatLng.of(this.geoLocation.lat, this.geoLocation.lon))
        .set("type", this.pieceType.name)
        .build()
    return pieceLocationEntity
}