package me.interair.wi.word

import reactor.core.publisher.Mono

interface WordsRepository {

    fun findByDocumentWord(word: String): Mono<WordReport>
    fun saveWord(wordData: WordData)
}