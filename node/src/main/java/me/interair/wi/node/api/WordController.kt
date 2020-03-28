package me.interair.wi.node.api

import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RequestMapping(path = ["/word"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class WordController(
        val repository: WordsRepository
) {
    @GetMapping(path = ["/{word}"])
    fun get(@PathVariable("word") word: String): Mono<WordReport> {
        return repository.findByDocumentWord(word)
    }

    @PutMapping
    fun save(@RequestBody wordData: WordData) {
        repository.saveWord(wordData)
    }
}