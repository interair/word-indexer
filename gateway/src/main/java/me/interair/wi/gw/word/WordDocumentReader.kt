package me.interair.wi.gw.word

import me.interair.wi.word.Ref
import me.interair.wi.word.WordData
import java.io.File
import java.text.BreakIterator
import java.util.*

class WordDocumentReader {

    fun read(file: File, consumer: WordConsumer): Int {
        val data = file.readText(Charsets.UTF_8)
        val bi = BreakIterator.getWordInstance(Locale.US)
        bi.setText(data)
        var count = 0
        var lastIndex = bi.first()
        while (lastIndex != BreakIterator.DONE) {
            val firstIndex: Int = lastIndex
            lastIndex = bi.next()
            if (lastIndex != BreakIterator.DONE
                    && Character.isLetter(data[firstIndex])) {
                val word: String = data.substring(firstIndex, lastIndex).toLowerCase()
                consumer.invoke(WordData(word, Ref(file.name)))
                count++
            }
        }
        return count
    }

}
typealias WordConsumer = (WordData) -> Unit
