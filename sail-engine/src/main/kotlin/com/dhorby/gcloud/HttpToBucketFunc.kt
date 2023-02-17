package com.dhorby.gcloud

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import java.util.logging.Logger

class HttpToBucketFunc : HttpFunction {

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {
        response.writer.write("Hello world!")
    }

    companion object {
        private val logger = Logger.getLogger(HttpToBucketFunc::class.java.name)
    }

}



