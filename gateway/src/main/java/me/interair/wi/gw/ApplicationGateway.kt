package me.interair.wi.gw

import me.interair.wi.config.api.WebConfiguration
import me.interair.wi.gw.configuration.GatewayConfiguration
import me.interair.wi.gw.configuration.GatewayProperties
import me.interair.wi.gw.docs.DocumentConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Import
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.scheduling.annotation.EnableScheduling

/**
 */
@Import(
        PropertySourcesPlaceholderConfigurer::class,
        ConfigurationPropertiesAutoConfiguration::class,
        WebConfiguration::class,
        DocumentConfiguration::class,
        GatewayConfiguration::class,
        GatewayProperties::class
)
@EnableHystrix
@EnableScheduling
@EnableDiscoveryClient
@SpringBootConfiguration
class ApplicationGateway

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationGateway::class.java, *args)
}