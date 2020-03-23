package me.interair.wi.config.node

data class NodeInfo(
        val startPartition: Int,
        val endPartition: Int,
        val health: Boolean,
        val nodeUrl: String,
        val nodePort: Int = 8080
)