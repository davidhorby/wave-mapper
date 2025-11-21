package com.dhorby.wavemapper.storage

import com.dhorby.wavemapper.model.GeoLocation
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.dhorby.wavemapper.storage.EntityKind.PIECE_LOCATION
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.LatLng

fun PieceLocation.toEntity(): Entity {
    val key = Key.newBuilder("test", PIECE_LOCATION.kind, "${this.id}").build()
    val pieceLocationEntity: Entity =
        Entity
            .newBuilder(key)
            .set("id", this.id)
            .set("name", this.name)
            .set("location", LatLng.of(this.geoLocation.lat, this.geoLocation.lon))
            .set("type", this.pieceType.name)
            .build()
    return pieceLocationEntity
}

fun Entity.toPieceLocation(): PieceLocation {
    val location: LatLng = this.getLatLng("location")
    val lat = location.latitude
    val lng = location.longitude
    return PieceLocation(
        id = this.properties["id"]?.get().toString(),
        name = this.properties["name"]?.get().toString(),
        pieceType = PieceType.valueOf(this.properties["type"]?.get().toString()),
        geoLocation = GeoLocation(lat, lng),
    )
}
