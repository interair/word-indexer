package me.interair.wi.gw.word

import me.interair.wi.word.WordData
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals


internal class WordDocumentReaderTest {

    @Rule
    var temporaryFolder = TemporaryFolder()

    @Test
    fun read() {
        temporaryFolder.create()
        val newFile = temporaryFolder.newFile("files")
        newFile.appendText("Loretem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. " +
                "It was popularised in the 1960s with the release of Letraset sheets containing " +
                "Lorem Ipsum passages, and more recently with desktop publishing software " +
                "like Aldus PageMaker including versions of Lorem Ipsum.")
        val reader = WordDocumentReader()
        val setOfWords = HashSet<WordData>()
        reader.read(newFile, consumer = {
            setOfWords.add(it)
        })
        assertEquals(65, setOfWords.size)
    }
}