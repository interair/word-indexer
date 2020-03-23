package me.interair.wi.node.config

import me.interair.wi.node.rest.WordController
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
@ComponentScan(basePackageClasses = [WordController::class])
class NodeConfiguration {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder, properties: NodeProperties): RestTemplate? {
        return builder.rootUri(properties.endpoint).build()
    }

    @Bean
    fun restTemplateBuilder(): RestTemplateBuilder {
        return RestTemplateBuilder()
    }
}
