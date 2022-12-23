import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DataStoreClient {

    private val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

    fun writeToDatastore(pieceLocation: PieceLocation) {

        // The kind for the new entity
        val kind = "PieceLocation"
        // The name/ID for the new entity
        val name = "create"
        // The Cloud Datastore key for the new entity
        val taskKey: Key = datastore.newKeyFactory().setKind(kind).newKey(name)
        // Get the pieceLocation as Json
        val pieceLocationAsString = Json.encodeToString(pieceLocation)
        // Prepares the new entity
        val pieceLocation: Entity = Entity.newBuilder(taskKey).set(pieceLocation.name, pieceLocationAsString).build()

        // Saves the entity
        datastore.put(pieceLocation)
    }
}