package me.interair.wi.gw.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "gw")
data class GatewayProperties(
        var partitions: Int = 256,
        var storagePath: String = "/tmp",
        var maxStorageSizeMb: Long = 1024
)