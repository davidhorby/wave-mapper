package com.dhorby.gcloud

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.tools.getFirstJsonObject
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable
import java.util.logging.Logger

data class Command(val name:String, val command:String): Serializable

class BucketToDSFunc : BackgroundFunction<BucketToDSFunc.GCSEvent> {

    val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

    class GCSEvent {
        var bucket: String? = null
        var name: String? = null
        var metageneration: String? = null
    }

    override fun accept(gcsEvent: GCSEvent, context: Context?) {

        // Validate parameters
        val bucket: String = gcsEvent.bucket ?: throw IllegalArgumentException("Missing bucket parameter")
        val filename: String = gcsEvent.name ?: throw IllegalArgumentException("Missing name parameter")
        val storage: Storage = StorageOptions.getDefaultInstance().service
        val jsonString: String = getFirstJsonObject(storage, bucket, filename)
        val pieceLocation = Json.decodeFromString<PieceLocation>(jsonString)
        pieceLocation.writeToDatastore()
    }

    companion object {
        private val logger = Logger.getLogger(BucketToDSFunc::class.java.name)
    }



    private fun Command.writeToDatastore() {

        // The kind for the new entity
        val kind = "Command"
        // The name/ID for the new entity
        val name = "create"
        // The Cloud Datastore key for the new entity
        val taskKey: Key = datastore.newKeyFactory().setKind(kind).newKey(name)

        // Prepares the new entity
        val command: Entity = Entity.newBuilder(taskKey).set(this.name, this.command).build()

        // Saves the entity
        datastore.put(command)
    }

    private fun PieceLocation.writeToDatastore() {

        // The kind for the new entity
        val kind = "PieceLocation"
        // The name/ID for the new entity
        val name = "create"
        // The Cloud Datastore key for the new entity
        val taskKey: Key = datastore.newKeyFactory().setKind(kind).newKey(name)
        // Get the pieceLocation as Json
        val pieceLocationAsString = Json.encodeToString(this)
        // Prepares the new entity
        val pieceLocation: Entity = Entity.newBuilder(taskKey).set(this.name, pieceLocationAsString).build()

        // Saves the entity
        datastore.put(pieceLocation)
    }
}



