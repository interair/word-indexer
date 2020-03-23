package me.interair.wi.word

import java.io.Serializable

data class WordReport(
        val word: String,
        val refs: MutableSet<Ref> = HashSet()
): Serializable {

    fun add(source: Ref) {
        refs.add(source)
    }

    fun merge(previous: WordReport) {
        refs.addAll(previous.refs)
    }
}