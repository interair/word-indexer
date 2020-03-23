package me.interair.wi.gw.configuration

import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.gw.partitioning.RestDiscoveryClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfiguration {

//    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator? {
        return builder.routes()
                .route("default") { r ->
                    r.path("/**")
                            .uri("lb://node")
                }
                .build()
    }

    @Bean
    fun discoveryClient(properties: GatewayProperties): RestDiscoveryClient {
        return RestDiscoveryClient(properties.partitions)
    }

    @Bean
    fun partitionResolver(properties: GatewayProperties): PartitionResolver {
        return PartitionResolver(properties.partitions)
    }

}