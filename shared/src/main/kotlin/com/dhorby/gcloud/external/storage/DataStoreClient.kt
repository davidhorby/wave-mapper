package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.DatastoreEvent
import com.google.cloud.datastore.*
import org.http4k.events.Event


class DataStoreClient(val events: (Event) -> Unit, private val datastore: Datastore): Storable {
    

    override fun writeToDatastore(kind:String, pieceLocation: PieceLocation) {

        events(DatastoreEvent("writing to datastore"))

        // The Cloud Datastore key for the new entity
        val key: Key = datastore.newKeyFactory().setKind(kind).newKey(pieceLocation.id)
        // Prepares the new entity
        val pieceLocationEntity: Entity = Entity
            .newBuilder(key)
            .set("id", pieceLocation.id)
            .set("name", pieceLocation.name)
            .set("location", LatLng.of(pieceLocation.geoLocation.lat, pieceLocation.geoLocation.lon))
            .set("type", pieceLocation.pieceType.name)
            .build()

        // Saves the entity
        datastore.put(pieceLocationEntity)
    }

    override fun getAllEntitiesOfKind(kind: String): List<Entity> {

        events(DatastoreEvent("Reading from to datastore"))
        val query: Query<Entity> = Query.newEntityQueryBuilder()
            .setKind("PieceLocation")
            .build()
        val queryResults: QueryResults<Entity> = datastore.run(query)


        val results = mutableListOf<Entity>()
        while (queryResults.hasNext()) {
            results += queryResults.next()
        }
        return results
    }

    override fun getAllEntitiesOfType(kind:String,type: PieceType): MutableList<Entity> {

        events(DatastoreEvent("Reading from to datastore"))
        val query: Query<Entity> = Query.newEntityQueryBuilder()
            .setKind(kind)
            .setFilter(
                StructuredQuery.CompositeFilter.and(
                    StructuredQuery.PropertyFilter.eq("type", type.name)
                )
            )
            .build()
        datastore.put()
        val queryResults: QueryResults<Entity> = datastore.run(query)


        val results = mutableListOf<Entity>()
        while (queryResults.hasNext()) {
            results += queryResults.next()
        }
        return results
    }

    override fun readFromDatastore(kind: String, name: String): Entity? {
        val key = datastore.newKeyFactory().setKind(kind).newKey(name)
        return datastore.get(key)
    }

    fun clearDatastore(kind: String = "PieceLocation") {
        events(DatastoreEvent("Clearing datastore"))
        val allEntitiesByKind = getAllEntitiesByKind(datastore, kind)
        val iterator = allEntitiesByKind.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            datastore.delete(entity.key)
        }
    }

    private fun getAllEntitiesByKind(datastore: Datastore, kind: String): Sequence<Entity> {
        val query: Query<Entity> = Query.newEntityQueryBuilder()
            .setKind(kind)
            .build()

        val results = datastore.run(query)

        return results.asSequence()
    }
}


