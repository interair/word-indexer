package me.interair.wi.word

interface WordsRepository {

    fun findByDocumentWord(word: String): WordReport
    fun saveWord(wordData: WordData)
}