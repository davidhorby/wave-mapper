package com.dhorby.gcloud.tools

import com.google.api.gax.paging.Page
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream


fun getFirstJsonObject(storage: Storage, bucket: String, filename: String): String {

    val blobs: Page<Blob> = storage.list(
        bucket,
        Storage.BlobListOption.prefix(filename)
    )
    val jsonObjects = blobs.values.map { blob ->
        gUnzip(blob.getContent())
    }
    return  jsonObjects.first()
}

fun gUnzip(content: ByteArray): String =
    GZIPInputStream(content.inputStream()).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
