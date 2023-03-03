import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DataStoreClient {

    private val datastore: Datastore  by lazy {
        if (Settings.ENV == "local") {
            DatastoreOptions.newBuilder()
                .setHost(Settings.HOST)
                .setProjectId(Settings.PROJECT_ID)
                .build()
                .service
        } else {
            DatastoreOptions.getDefaultInstance().service
        }
    }

    private val LOG: Logger = LoggerFactory.getLogger(DataStoreClient::class.java)

    fun writeToDatastore(pieceLocation: PieceLocation) {

        LOG.info("Writing to datastore")

        // The kind for the new entity
        val kind = "PieceLocation"
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
        datastore.put(pieceLocationEntity)

        LOG.info("Writing to datastore" + DatastoreOptions.getDefaultInstance().projectId)

        val entity = datastore.get(key)
        entity.properties.forEach { t, u -> LOG.info(t.toString() + ":" + u.toString()) }
    }

    fun getKeysOfKind(kind: String, type: String): MutableList<Entity> {


        LOG.info("Reading from datastore" + DatastoreOptions.getDefaultInstance().projectId)
        val query: Query<Entity> = Query.newEntityQueryBuilder()
            .setKind(kind)
            .setFilter(
                StructuredQuery.CompositeFilter.and(
                    StructuredQuery.PropertyFilter.eq("type", type)
                )
            )
            .build()
        val queryResults: QueryResults<Entity> = datastore.run(query)


        val results = mutableListOf<Entity>()
        while (queryResults.hasNext()) {
            results += queryResults.next()
        }
        return results
    }

    fun readFromDatastore(kind: String = "PieceLocation", name: String = "create"): Entity? {
        val key = datastore.newKeyFactory().setKind(kind).newKey(name)
        return datastore.get(key)
    }
}


