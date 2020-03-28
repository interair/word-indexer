package me.interair.wi.config.node

data class NodeInfo(
        val startPartition: Int,
        val endPartition: Int,
        val health: Boolean,
        val nodeUrl: String = "localhost",
        val nodePort: Int = 8081
)