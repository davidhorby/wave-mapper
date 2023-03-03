package com.dhorby.gcloud

import DataStoreClient.getKeysOfKind
import DataStoreClient.writeToDatastore
import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.model.PieceLocation
import com.google.cloud.datastore.Entity
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.gson.Gson
import com.google.gson.JsonObject;
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HttpToFirestoreFunc : HttpFunction {

    // Use GSON (https://github.com/google/gson) to parse JSON content.
    private val gson = Gson()

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {

        val LOG: Logger = LoggerFactory.getLogger(HttpToFirestoreFunc::class.java)

        LOG.info("env=" + Settings.ENV + "host=" + Settings.HOST + ":" + Settings.PROJECT_ID)

        val contentType = request.contentType.orElse("")
        LOG.info("Context type --->>>" + contentType)
        val jsonObject: JsonObject = gson.fromJson(request.reader, JsonObject::class.java)
        val jsonString = jsonObject.toString()
        val pieceLocation = Json.decodeFromString<PieceLocation>(jsonString)
        writeToDatastore(pieceLocation)

        val sharks: MutableList<Entity> = getKeysOfKind("PieceLocation", "SHARK")
        val allSharks = sharks.first().toString()
        response.writer.write("Size:" + sharks.size)
    }



}



