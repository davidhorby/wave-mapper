package com.dhorby.wavemapper.storage

import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.LatLng
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.QueryResults
import com.google.cloud.datastore.StructuredQuery

class DataStoreClient(
    private val datastore: Datastore,
) : Storable {
    override fun add(
        kind: EntityKind,
        pieceLocation: PieceLocation,
    ) {
        val key: Key = datastore.newKeyFactory().setKind(kind.kind).newKey(pieceLocation.id)
        val pieceLocationEntity: Entity =
            Entity
                .newBuilder(key)
                .set("id", pieceLocation.id)
                .set("name", pieceLocation.name)
                .set("location", LatLng.of(pieceLocation.geoLocation.lat, pieceLocation.geoLocation.lon))
                .set("type", pieceLocation.pieceType.name)
                .build()

        datastore.put(pieceLocationEntity)
    }

    override fun getAllEntitiesOfKind(kind: EntityKind): List<Entity> {
        val query: Query<Entity> =
            Query
                .newEntityQueryBuilder()
                .setKind(kind.kind)
                .build()
        val queryResults: QueryResults<Entity> = datastore.run(query)

        val results = mutableListOf<Entity>()
        while (queryResults.hasNext()) {
            results += queryResults.next()
        }
        return results
    }

    override fun getAllEntitiesOfType(
        kind: EntityKind,
        type: PieceType,
    ): MutableList<Entity> {
        val query: Query<Entity> =
            Query
                .newEntityQueryBuilder()
                .setKind(kind.kind)
                .setFilter(
                    StructuredQuery.CompositeFilter.and(
                        StructuredQuery.PropertyFilter.eq("type", type.name),
                    ),
                ).build()

        val queryResults: QueryResults<Entity> = datastore.run(query)

        val results = mutableListOf<Entity>()
        while (queryResults.hasNext()) {
            results += queryResults.next()
        }
        return results
    }

    override fun get(
        kind: EntityKind,
        name: String,
    ): Entity? {
        val key = datastore.newKeyFactory().setKind(kind.kind).newKey(name)
        return datastore.get(key)
    }

    override fun clearDatastore(kind: EntityKind) {
        val allEntitiesByKind = getAllEntitiesByKind(datastore, kind.kind)
        val iterator = allEntitiesByKind.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            datastore.delete(entity.key)
        }
    }

    override fun deleteEntity(
        kind: EntityKind,
        name: String,
    ) {
        val key: Key = datastore.newKeyFactory().setKind(kind.kind).newKey(name)
        datastore.delete(key)
    }

    override fun getEntity(
        kind: EntityKind,
        keyValue: String,
    ): Entity? {
        val key: Key = datastore.newKeyFactory().setKind(kind.kind).newKey(keyValue)
        return datastore.get(key)
    }

    private fun getAllEntitiesByKind(
        datastore: Datastore,
        kind: String,
    ): Sequence<Entity> {
        val query: Query<Entity> =
            Query
                .newEntityQueryBuilder()
                .setKind(kind)
                .build()

        val results = datastore.run(query)

        return results.asSequence()
    }
}
