package com.dhorby.wavemapper.storage

import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.google.cloud.datastore.Entity

interface Storable {
    fun get(
        kind: EntityKind,
        name: String,
    ): Entity?

    fun add(
        kind: EntityKind,
        pieceLocation: PieceLocation,
    )

    fun getAllEntitiesOfKind(kind: EntityKind): List<Entity>

    fun getAllEntitiesOfType(
        kind: EntityKind,
        type: PieceType,
    ): MutableList<Entity>

    fun clearDatastore(kind: EntityKind)

    fun deleteEntity(
        kind: EntityKind,
        name: String,
    )

    fun getEntity(
        kind: EntityKind,
        keyValue: String,
    ): Entity?
}
