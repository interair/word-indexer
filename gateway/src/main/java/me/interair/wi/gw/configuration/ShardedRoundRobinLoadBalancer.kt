package me.interair.wi.gw.configuration

import org.springframework.beans.factory.ObjectProvider
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.cloud.loadbalancer.support.SimpleObjectProvider
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class ShardedReactiveLoadBalancerFactory(val discoveryClient: DiscoveryClient) : LoadBalancerClientFactory() {

    override fun getInstance(serviceId: String?): ReactiveLoadBalancer<ServiceInstance> {
        val simpleObjectProvider: ObjectProvider<ServiceInstanceListSupplier> = SimpleObjectProvider(ServiceInstanceListSupplierDelegate(serviceId, discoveryClient))
        return RoundRobinLoadBalancer(simpleObjectProvider, serviceId)
    }
}

class ServiceInstanceListSupplierDelegate(val id: String?, val discoveryClient: DiscoveryClient) :
        ServiceInstanceListSupplier {

    private val subscribeOn = Flux
            .defer { Flux.fromIterable(discoveryClient.getInstances(serviceId)) }
            .subscribeOn(Schedulers.boundedElastic())

    override fun getServiceId(): String? {
        return id
    }

    override fun get(): Flux<List<ServiceInstance>> {
        return subscribeOn.collectList().flux()
    }
}
