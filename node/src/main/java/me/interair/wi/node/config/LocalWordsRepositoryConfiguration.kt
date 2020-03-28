package me.interair.wi.node.config

import me.interair.wi.node.words.LocalWordsRepository
import me.interair.wi.word.WordReport
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LocalWordsRepositoryConfiguration {

    @Bean
    fun cacheManager(nodeProperties: NodeProperties): CacheManager {
        return CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(nodeProperties.cachePath))
                .withCache("persistent-cache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String::class.java, WordReport::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10_000_000, EntryUnit.ENTRIES)
                                        .disk(nodeProperties.cacheSizeInMb, MemoryUnit.MB, true))
                )
                .build(true)
    }

    @Bean
    fun cache(cacheManager: CacheManager): Cache<String, WordReport> {
        return cacheManager.getCache("persistent-cache", String::class.java, WordReport::class.java)
    }

    @Bean
    fun localWordsRepository(cache: Cache<String, WordReport>): LocalWordsRepository {
        return LocalWordsRepository(cache)
    }
}