package com.dhorby.gcloud

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import java.io.BufferedWriter
import java.io.IOException

class HelloWorld : HttpFunction {
    @Throws(IOException::class)
    override fun service(request: HttpRequest, response: HttpResponse) {
        val writer: BufferedWriter = response.getWriter()
        writer.write("Hello World! This is a test")
    }
}
