package me.interair.wi.gw.api

import me.interair.wi.config.node.NodeInfo
import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.gw.partitioning.RestDiscoveryClient
import org.springframework.cloud.client.ServiceInstance
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/node"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class NodesController(val discovery: RestDiscoveryClient, val partitionResolver: PartitionResolver) {

    @GetMapping("/list")
    fun list(): Set<ServiceInstance> {
        return discovery.nodes()
    }

    @GetMapping(path = ["/{id}"])
    fun item(@PathVariable("id") id: String): List<ServiceInstance> {
        return discovery.getInstances(id)
    }

    @GetMapping(path = ["partition/{word}"])
    fun nodesForWord(@PathVariable("word") word: String): List<ServiceInstance> {
        return discovery.getInstances(partitionResolver.resolvePartition(word))
    }

    @PutMapping
    fun update(@RequestBody info: NodeInfo) {
        discovery.update(info)
    }
}
