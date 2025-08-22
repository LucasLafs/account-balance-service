package com.challenge.accountbalanceservice.testing.extensions.sqs

import io.awspring.cloud.sqs.operations.SqsTemplate
import org.elasticmq.rest.sqs.SQSRestServer
import org.elasticmq.rest.sqs.SQSRestServerBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region.SA_EAST_1
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import java.net.URI

@TestConfiguration
class SQSEmbeddedConfiguration {

    companion object {
        private var server: SQSRestServer? = null
        const val HOST = "http://localhost"
        const val PORT = 4645

        fun start() {
            if (server == null) {
                server = SQSRestServerBuilder
                    .withPort(PORT)
                    .start()
            }
        }

        fun stop() {
            server?.stopAndWait()
            server = null
        }
    }

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient {
        start()

        return SqsAsyncClient.builder()
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test")
                )
            )
            .endpointOverride(URI.create("$HOST:$PORT"))
            .region(SA_EAST_1)
            .build()
    }

    @Bean
    fun sqsTemplate(sqsAsyncClient: SqsAsyncClient): SqsTemplate {
        return SqsTemplate.builder()
            .sqsAsyncClient(sqsAsyncClient)
            .build()
    }
}
