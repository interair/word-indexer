package me.interair.wi.node.lookup

import me.interair.wi.config.node.NodeInfo
import me.interair.wi.node.config.NodeProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.client.RestTemplate

class LookupService(val rest: RestTemplate, val props: NodeProperties) {

    @Scheduled(fixedDelay = 1000)
    fun sendInfo() {
        rest.put("/node", NodeInfo(props.startPartition, props.endPartition, true, props.selfUrl))

    }
}