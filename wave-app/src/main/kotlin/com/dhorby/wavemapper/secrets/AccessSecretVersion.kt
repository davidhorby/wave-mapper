package com.dhorby.wavemapper.secrets

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName

object AccessSecretVersion {
    fun accessSecretVersion(secretId: String): String? {
        val projectId = "analytics-springernature"
        val versionId = "latest"
        return accessSecretVersion(projectId, secretId, versionId)
    }

    // Access the payload for the given secret version if one exists. The version
    // can be a version number as a string (e.g. "5") or an alias (e.g. "latest").
    private fun accessSecretVersion(
        projectId: String?,
        secretId: String?,
        versionId: String?,
    ): String? {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        SecretManagerServiceClient.create().use { client ->
            val secretVersionName = SecretVersionName.of(projectId, secretId, versionId)

            // Access the secret version.
            val response = client.accessSecretVersion(secretVersionName)

            return response.payload.data.toStringUtf8()
        }
    }
}
