package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.google.cloud.datastore.Entity

interface Storable {
    fun readFromDatastore(kind: EntityKind, name: String): Entity?
    fun writeToDatastore(kind: EntityKind, pieceLocation: PieceLocation)
    fun getAllEntitiesOfKind(kind: EntityKind):List<Entity>
    fun getAllEntitiesOfType(kind: EntityKind, type: PieceType): MutableList<Entity>
    fun clearDatastore(kind: EntityKind)
    fun deleteEntity(kind: EntityKind, name: String)
    fun getEntity(kind: EntityKind, keyValue: String):Entity?
}