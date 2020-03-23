package me.interair.wi.gw.rest

import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 */
@RequestMapping(path = ["/word"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class WordController(
        val repository: WordsRepository
) {
    @GetMapping(path = ["/{word}"])
    fun get(@PathVariable("word") word: String): WordReport {
        return repository.findByDocumentWord(word)
    }
}
