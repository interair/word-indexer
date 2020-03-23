package me.interair.wi.node.rest

import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/word"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class WordController(
        val repository: WordsRepository
) {
    @GetMapping(path = ["/{word}"])
    fun get(@PathVariable("word") word: String): WordReport {
        return repository.findByDocumentWord(word)
    }

    @PutMapping
    fun save(@RequestBody wordData: WordData) {
        repository.saveWord(wordData)
    }
}