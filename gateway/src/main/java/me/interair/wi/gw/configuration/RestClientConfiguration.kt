package me.interair.wi.gw.configuration

import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.gw.rest.DocumentController
import me.interair.wi.gw.word.RestWordsRepository
import me.interair.wi.gw.word.WordDocumentReader
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan(basePackageClasses = [DocumentController::class])
class RestClientConfiguration {

    @LoadBalanced
    @Bean
    fun getRestTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun restWordsRepository(restTemplate: RestTemplate, partitionResolver: PartitionResolver): RestWordsRepository {
        return RestWordsRepository(restTemplate, partitionResolver)
    }

    @Bean
    fun wordDocumentReader(): WordDocumentReader {
        return WordDocumentReader()
    }
}