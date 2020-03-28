package me.interair.wi.node.words

import me.interair.wi.word.Ref
import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import org.ehcache.Cache
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class LocalWordsRepositoryTest {

    @Test
    fun findByDocumentWord() {
        val repository = LocalWordsRepository(cache())
        repository.saveWord(WordData("test", Ref("test1.txt")))
        repository.saveWord(WordData("test", Ref("test2.txt")))
        val findByDocumentWord = repository.findByDocumentWord("test")
        val report = findByDocumentWord.block()
        assertNotNull(report)
        assertEquals(2, report.refs.size)
    }

    fun cache(): Cache<String, WordReport> {
        return CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("test"))
                .withCache("persistent-cache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String::class.java, WordReport::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                )
                .build(true).getCache("persistent-cache", String::class.java, WordReport::class.java)
    }
}
