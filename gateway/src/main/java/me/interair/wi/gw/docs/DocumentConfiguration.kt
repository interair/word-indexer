package me.interair.wi.gw.docs

import me.interair.wi.gw.configuration.GatewayProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocumentConfiguration {

    @Bean
    fun docsStorage(properties: GatewayProperties) = DocsStorage(properties.storagePath)
}
