import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.*


object DataStoreClient {

    private val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

    fun writeToDatastore(pieceLocation: PieceLocation) {

        // The kind for the new entity
        val kind = "PieceLocation"
        // The name/ID for the new entity
        val name = "create"
        // The Cloud Datastore key for the new entity
        val taskKey: Key = datastore.newKeyFactory().setKind(kind).newKey(pieceLocation.id)
        // Prepares the new entity
        val pieceLocationEntity: Entity = Entity
            .newBuilder(taskKey)
            .set("id", pieceLocation.id)
            .set("name", pieceLocation.name)
            .set("location", LatLng.of(pieceLocation.geoLocation.lat, pieceLocation.geoLocation.lon))
            .set("type", pieceLocation.pieceType.name)
            .build()
        datastore

        // Saves the entity
        datastore.put(pieceLocationEntity)
    }

    fun getKeysOfKind(kind:String, type:String): MutableList<Entity> {
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

    fun readFromDatastore(kind:String = "PieceLocation",name:String = "create" ): Entity? {
        val key = datastore.newKeyFactory().setKind(kind).newKey(name)
        return datastore.get(key)
    }
}