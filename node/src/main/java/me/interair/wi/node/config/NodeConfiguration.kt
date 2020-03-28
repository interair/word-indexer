package me.interair.wi.node.config

import me.interair.wi.node.lookup.LookupService
import me.interair.wi.node.api.WordController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ComponentScan(basePackageClasses = [WordController::class])
class NodeConfiguration {

    @Bean
    fun webClient(nodeProperties: NodeProperties, webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder
                .baseUrl(nodeProperties.endpointUrl)
                .build()
    }

    @Bean
    fun lookupService(webClient: WebClient, nodeProperties: NodeProperties): LookupService {
        return LookupService(webClient, nodeProperties)
    }
}
