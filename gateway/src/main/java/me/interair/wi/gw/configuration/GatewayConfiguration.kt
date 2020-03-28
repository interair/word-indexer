package me.interair.wi.gw.configuration

import me.interair.wi.gw.api.DocumentController
import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.gw.partitioning.RestDiscoveryClient
import me.interair.wi.gw.word.RestWordsRepository
import me.interair.wi.gw.word.WordDocumentReader
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ComponentScan(basePackageClasses = [DocumentController::class])
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

    @Bean
    @Primary
    fun loadBalancerClientFactory(discoveryClient: DiscoveryClient): LoadBalancerClientFactory {
        return ShardedReactiveLoadBalancerFactory(discoveryClient)
    }

    @Bean
    @Primary
    fun lbFunction(loadBalancerClientFactory: LoadBalancerClientFactory): ReactorLoadBalancerExchangeFilterFunction {
        return ReactorLoadBalancerExchangeFilterFunction(loadBalancerClientFactory)
    }

    @Bean
    fun restWordsRepository(webClientBuilder: WebClient.Builder,
                            partitionResolver: PartitionResolver,
                            discoveryClient: RestDiscoveryClient,
                            lbFunction: ReactorLoadBalancerExchangeFilterFunction): RestWordsRepository {
        return RestWordsRepository(webClientBuilder.clone().filter(lbFunction).build(), webClientBuilder.clone(), partitionResolver, discoveryClient)
    }

    @Bean
    fun wordDocumentReader(): WordDocumentReader {
        return WordDocumentReader()
    }
}
