package me.interair.wi.gw.partitioning

import kotlin.math.abs

class PartitionResolver(val partitions: Int) {

    fun resolvePartition(id: String): String {
        return abs(id.hashCode() % partitions).toString()
    }
}