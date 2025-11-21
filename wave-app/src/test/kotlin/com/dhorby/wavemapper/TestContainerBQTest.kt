package com.dhorby.wavemapper

import com.google.cloud.NoCredentials
import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.Dataset
import com.google.cloud.bigquery.DatasetInfo
import com.google.cloud.bigquery.Field
import com.google.cloud.bigquery.Schema
import com.google.cloud.bigquery.StandardSQLTypeName
import com.google.cloud.bigquery.StandardTableDefinition
import com.google.cloud.bigquery.TableDefinition
import com.google.cloud.bigquery.TableId
import com.google.cloud.bigquery.TableInfo
import org.testcontainers.containers.BigQueryEmulatorContainer
import kotlin.test.Test


class TestContainerBQTest {

    val container: BigQueryEmulatorContainer = BigQueryEmulatorContainer("ghcr.io/goccy/bigquery-emulator:0.4.3")

    val schema: Schema? =
        Schema.of(
            Field.of("stringField", StandardSQLTypeName.STRING),
            Field.of("booleanField", StandardSQLTypeName.BOOL)
        )
    val datasetName = "MY_DATASET_NAME"
    val tableName = "MY_TABLE_NAME"

    @Test
    fun testSimple() {
        container.start()
        val url: String? = container.getEmulatorHttpEndpoint()
        val options: BigQueryOptions = BigQueryOptions
            .newBuilder()
            .setProjectId(container.getProjectId())
            .setHost(url)
            .setLocation(url)
            .setCredentials(NoCredentials.getInstance())
            .build()
        val bigQuery: BigQuery = options.getService()

        val datasetInfo = DatasetInfo.newBuilder(datasetName).build()
        val newDataset: Dataset? = bigQuery.create(datasetInfo)
        val newDatasetName: String? = newDataset?.getDatasetId()?.getDataset()
        val tableId = TableId.of(datasetName, tableName)
        val tableDefinition: TableDefinition? = StandardTableDefinition.of(schema)
        val tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build()
        bigQuery.create(tableInfo)
        println("BigQuery Endpoint: $url")

    }
}
