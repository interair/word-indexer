package me.interair.wi.gw.partitioning

import me.interair.wi.config.node.NodeInfo
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RestDiscoveryClientTest {

    @Test
    fun getInstances() {
        val client = RestDiscoveryClient(256)
        client.update(NodeInfo(0, 255, true, "test.org"))
        assertEquals(256, client.nodes().size)
        val instances = client.getInstances("1")
        assertEquals("test.org", instances[0].host)
        val instancesEnd = client.getInstances("255")
        assertEquals("test.org", instancesEnd[0].host)

    }
}