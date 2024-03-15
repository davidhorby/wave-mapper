package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.google.cloud.datastore.Entity

interface Storable {
    fun readFromDatastore(kind: String, name: String): Entity?
    fun writeToDatastore(kind: String, pieceLocation: PieceLocation)
    fun getAllEntitiesOfKind(kind: String):List<Entity>
    fun getAllEntitiesOfType(kind: String, type: PieceType): MutableList<Entity>
    fun clearDatastore(kind: String)
}