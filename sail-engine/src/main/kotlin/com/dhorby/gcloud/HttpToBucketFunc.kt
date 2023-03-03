package com.dhorby.gcloud

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.gson.Gson
import com.google.gson.JsonObject;
import java.util.logging.Logger

class HttpToBucketFunc : HttpFunction {

    // Use GSON (https://github.com/google/gson) to parse JSON content.
    private val gson = Gson()

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {
        val contentType = request.contentType.orElse("")
        println(contentType)
//        response.getWriter().write("Hello, World\n");
        val jsonObject: JsonObject = gson.fromJson(request.reader, JsonObject::class.java)
//        if (body.has("name")) {
//            name = body["name"].asString
//        }

//        response.writer.write(jsonObject.get("test").asString)
        response.writer.write("Message received\n" + jsonObject.toString())
    }

    companion object {
        private val logger = Logger.getLogger(HttpToBucketFunc::class.java.name)
    }

}



