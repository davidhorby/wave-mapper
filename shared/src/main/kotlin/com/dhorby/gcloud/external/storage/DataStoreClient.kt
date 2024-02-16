package com.dhorby.gcloud.external.storage

import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.DatastoreEvent
import com.google.cloud.datastore.*
import com.google.cloud.datastore.testing.LocalDatastoreHelper
import org.http4k.events.Event

//import org.slf4j.Logger
//import org.slf4j.LoggerFactory

class DataStoreClient(val events: (Event) -> Unit, private val datastore: Datastore): Storable {

    private val datastore2: Datastore  by lazy {
//        LOG.info("Environment -->> ${Settings.ENV}" )
        if (Settings.ENV == "local") {
            val localDatastoreHelper = LocalDatastoreHelper
                .newBuilder()
                .setPort(8081)
                .setConsistency(1.0) // If the consistency is not 1.0, the data may not be there yet
                .build()
            localDatastoreHelper.start()
            DatastoreOptions.newBuilder()
                .setHost("localhost:8081")
                .setProjectId(Settings.PROJECT_ID)
                .build()
                .service
        } else {
            LocalDatastoreHelper.newBuilder()
            DatastoreOptions.getDefaultInstance().service
        }
    }



//    private val LOG: Logger = LoggerFactory.getLogger(com.dhorby.gcloud.external.storage.DataStoreClient::class.java)

    override fun writeToDatastore(kind:String, pieceLocation: PieceLocation): Entity? {

        events(DatastoreEvent("writing to datastore"))

//        LOG.info("Writing to datastore")

        // The kind for the new entity
//        val kind = "PieceLocation"
        // The name/ID for the new entity
        val name = "create"
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
        datastore

        // Saves the entity
        return datastore.put(pieceLocationEntity)

//        LOG.info("Writing to datastore" + DatastoreOptions.getDefaultInstance().projectId)

//        datastore.get(key).properties.forEach { (t, u) -> LOG.info("$t:$u") }
    }

    fun getPieces(type: PieceType): MutableList<Entity> {

        events(DatastoreEvent("Reading from to datastore"))
        val query: Query<Entity> = Query.newEntityQueryBuilder()
            .setKind("PieceLocation")
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


