package me.interair.wi.node.lookup

import me.interair.wi.config.node.NodeInfo
import me.interair.wi.node.config.NodeProperties
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

class LookupService(val rest: WebClient, val props: NodeProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 500)
    public fun sendInfo() {
        val nodeInfo = NodeInfo(props.startPartition, props.endPartition, true, props.selfName)
        rest
                .put()
                .uri("/node")
                .body(BodyInserters.fromValue(nodeInfo))
                .exchange()
                .subscribe { body -> log.debug("Send info to {}, result: {}", nodeInfo, body.statusCode())}
    }
}