package me.interair.wi.node.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "node")
data class NodeProperties(
        var startPartition: Int = 0,
        var endPartition: Int = 255,
        var endpointUrl: String = "http://localhost:8080",
        var selfName: String = "localhost",
        var cachePath: String = "/tmp/word/cache/",
        var cacheSizeInMb: Long = 1024
)