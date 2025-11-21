package com.dhorby.gcloud.external.junit

import com.google.cloud.NoCredentials
import com.google.cloud.ServiceOptions
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.testcontainers.containers.DatastoreEmulatorContainer
import org.testcontainers.utility.DockerImageName

open class DataStoreExtension :
    BeforeTestExecutionCallback,
    BeforeAllCallback,
    AfterAllCallback,
    ParameterResolver {
    val emulator: DatastoreEmulatorContainer =
        DatastoreEmulatorContainer(
            DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:441.0.0-emulators"),
        )

    lateinit var datastore: Datastore

    class Builder {
        fun build(): DataStoreExtension = DataStoreExtension()
    }

    override fun beforeTestExecution(context: ExtensionContext) {
        val options: DatastoreOptions =
            DatastoreOptions
                .newBuilder()
                .setHost(emulator.emulatorEndpoint)
                .setCredentials(NoCredentials.getInstance())
                .setRetrySettings(ServiceOptions.getNoRetrySettings())
                .setProjectId(emulator.getProjectId())
                .build()
        datastore = options.service
        store(context).put("datastore", options.service)
    }

    override fun beforeAll(extensionContext: ExtensionContext) {
        emulator.start()
    }

    override fun afterAll(extensionContext: ExtensionContext) {
        emulator.stop()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Boolean = parameterContext.parameter.type == Datastore::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Any? = if (supportsParameter(parameterContext, extensionContext)) store(extensionContext)["datastore"] else null

    private fun store(extensionContext: ExtensionContext) =
        with(extensionContext) {
            getStore(ExtensionContext.Namespace.create(requiredTestClass.name, requiredTestMethod.name))
        }
}
