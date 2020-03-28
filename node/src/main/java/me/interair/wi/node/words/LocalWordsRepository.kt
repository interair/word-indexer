package me.interair.wi.node.words

import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.ehcache.Cache
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class LocalWordsRepository(val cache: Cache<String, WordReport>) : WordsRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByDocumentWord(word: String): Mono<WordReport> {
        return Mono.just(cache[word.toLowerCase()] ?: WordReport(word))
    }

    /**
     * CAS update
     */
    override fun saveWord(w: WordData) {
        val current = WordReport(w.word)
        current.add(w.source)
        var previous = cache.putIfAbsent(w.word, current)
        if (previous != null) {
            current.merge(previous)
            while (!cache.replace(w.word, previous, current)) {
                previous = cache[w.word]
                current.merge(previous)
                log.info("Saved word: {}", w)
            }
        }
    }
}