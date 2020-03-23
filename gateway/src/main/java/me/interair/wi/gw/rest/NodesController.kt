package me.interair.wi.gw.rest

import me.interair.wi.config.node.NodeInfo
import me.interair.wi.gw.partitioning.RestDiscoveryClient
import org.springframework.cloud.client.ServiceInstance
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*

/**
 */
@RequestMapping(path = ["/node"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class NodesController(val discovery: RestDiscoveryClient, val discoveryClient: RestDiscoveryClient) {

    @GetMapping("/list")
    fun list(): Set<ServiceInstance> {
        return discovery.nodes()
    }

    @GetMapping(path = ["/{id}"])
    fun item(@PathVariable("id") id: String): List<ServiceInstance> {
        return discovery.getInstances(id)
    }

    @PutMapping
    fun update(info: NodeInfo) {
        discoveryClient.update(info)
    }
}
