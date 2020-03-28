package me.interair.wi.node

import me.interair.wi.config.api.WebConfiguration
import me.interair.wi.node.config.LocalWordsRepositoryConfiguration
import me.interair.wi.node.config.NodeConfiguration
import me.interair.wi.node.config.NodeProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.scheduling.annotation.EnableScheduling

/**
 */
@Import(
        PropertySourcesPlaceholderConfigurer::class,
        ConfigurationPropertiesAutoConfiguration::class,
        WebConfiguration::class,
        LocalWordsRepositoryConfiguration::class,
        NodeConfiguration::class,
        NodeProperties::class
)
@SpringBootConfiguration
@EnableScheduling
class NodeApplication

fun main(args: Array<String>) {
    SpringApplication.run(NodeApplication::class.java, *args)
}