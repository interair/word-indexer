package me.interair.wi.gw.partitioning

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Scheduler
import me.interair.wi.config.node.NodeInfo
import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RestDiscoveryClient(val partitions: Int) : DiscoveryClient {

    var mapping: Cache<String, Set<ServiceInstance>> = Caffeine.newBuilder()
            .initialCapacity(partitions)
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .recordStats()
            .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
            .build()

    override fun getServices(): List<String> {
        return (0..partitions).map { it.toString() }.toList()
    }

    override fun getInstances(serviceId: String): List<ServiceInstance> {
        return mapping.getIfPresent(serviceId)!!.toList()
    }

    override fun description(): String {
        return "partitions"
    }

    fun nodes(): Set<ServiceInstance> {
        return mapping.asMap().values.filter { it.isNotEmpty() }.flatMap { it.asIterable() }.toCollection(HashSet())
    }

    fun update(nodeInfo: NodeInfo) {
        val name = nodeInfo.startPartition.toString() + "_" + nodeInfo.endPartition.toString()
        (nodeInfo.startPartition..nodeInfo.endPartition).forEach {
            mapping.asMap().compute(it.toString()) { key: String, value: Set<ServiceInstance>? ->
                (value ?: HashSet()).plus(DefaultServiceInstance(key, name, nodeInfo.nodeUrl, nodeInfo.nodePort, false))
            }
        }
    }
}
