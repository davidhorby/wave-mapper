package com.dhorby.gcloud

import DataStoreClient.writeToDatastore
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.tools.getFirstJsonObject
import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
        val jsonString: String = getFirstJsonObject(storage, bucket, filename)
        val pieceLocation = Json.decodeFromString<PieceLocation>(jsonString)
        writeToDatastore(pieceLocation)
    }

    companion object {
        private val logger = Logger.getLogger(BucketToDSFunc::class.java.name)
    }

}



