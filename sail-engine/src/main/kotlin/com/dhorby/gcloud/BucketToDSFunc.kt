package com.dhorby.gcloud

import com.dhorby.gcloud.tools.getFirstJsonObject
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.Serializable
import java.util.logging.Logger

data class Command(val name:String, val command:String): Serializable

class BucketToDSFunc : BackgroundFunction<BucketToDSFunc.GCSEvent> {

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
        val jsonObject: String = getFirstJsonObject(storage, bucket, filename)
//        jsonObject.writeToDatastore()
    }

    companion object {
        private val logger = Logger.getLogger(BucketToDSFunc::class.java.name)
    }



    private fun Command.writeToDatastore() {
        val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

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
}



