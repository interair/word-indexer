package me.interair.wi.node.api

import me.interair.wi.node.config.NodeProperties
import org.ehcache.core.spi.service.StatisticsService
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/info"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class InfoController(
        val nodeProperties: NodeProperties,
        val statisticsService: StatisticsService
) {
    @GetMapping(path = ["/words-stat"])
    fun wordsStat(): Long {
        return statisticsService.getCacheStatistics("persistent-cache").cachePuts
    }

    @GetMapping(path = ["/node-info"])
    fun nodeStat(): NodeProperties {
        return nodeProperties;
    }
}