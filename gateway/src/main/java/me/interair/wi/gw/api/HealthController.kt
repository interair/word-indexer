package me.interair.wi.gw.api

import me.interair.wi.gw.partitioning.RestDiscoveryClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/health"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class HealthController(val discovery: RestDiscoveryClient) {

    @GetMapping
    fun health(): ResponseEntity<HealthResult> {
        val healthy = discovery.nodes().size.compareTo(discovery.partitions) == 0
        discovery.mapping.cleanUp()
        val result = HealthResult(healthy, discovery.partitions, discovery.nodes().size)
        return if (healthy) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity(result, HttpStatus.BAD_GATEWAY)
        }
    }

    data class HealthResult(
            val healthy: Boolean,
            val totalPartitions: Int,
            val availablePartitions: Int
    )
}
