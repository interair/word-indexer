package me.interair.wi.words

import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.ehcache.Cache

class LocalWordsRepository(val cache: Cache<String, WordReport>) : WordsRepository {

    override fun findByDocumentWord(word: String): WordReport {
        return cache[word] ?: WordReport(word)
    }

    override fun saveWord(wordData: WordData) {
        val current = WordReport(wordData.word)
        current.add(wordData.source)
        var previous = cache.putIfAbsent(wordData.word, current)
        if (previous != null) {
            current.merge(previous)
            while (!cache.replace(wordData.word, previous, current)) {
                previous = cache[wordData.word]
                current.merge(previous)
            }
        }
    }
}