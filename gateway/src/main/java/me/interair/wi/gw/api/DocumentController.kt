package me.interair.wi.gw.api

import me.interair.wi.gw.docs.DocsStorage
import me.interair.wi.gw.word.WordDocumentReader
import me.interair.wi.word.WordsRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

/**
 */
@RequestMapping(path = ["/doc"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
@RestController
class DocumentController(
        val repository: WordsRepository,
        val docStorage: DocsStorage,
        val reader: WordDocumentReader
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val MAX_DOC_SIZE = 20 * 1024 * 1024
    }

    @GetMapping("/list")
    fun list(): List<String> {
        return docStorage.getAvailable().map { it.name }
    }

    @GetMapping("/download")
    fun download(id: String): ResponseEntity<StreamingResponseBody> {
        val file = docStorage.get(id)
        if (!file.exists()) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''${file.name}")
        return ResponseEntity(StreamingResponseBody { os ->
            os.write(file.readBytes())
        }, headers, HttpStatus.OK)
    }

    @PostMapping("/upload-text", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun uploadText(
            @RequestParam(name = "id") id: String,
            @RequestBody txt: String
    ): WordsProcessResult {
        checkDocumentIsNew(id)
        return processData(fileName = id, data = txt.toByteArray())
    }

    private fun checkDocumentIsNew(id: String) {
        // we must not upload to already existed document!
        if (docStorage.get(id).exists()) {
            throw IllegalArgumentException("Document with $id already exists.")
        }
    }

    @PostMapping("/upload")
    fun uploadSource(
            @RequestPart("file") file: MultipartFile
    ): WordsProcessResult {
        checkDocumentIsNew(file.name)
        checkContentSize(file.size)

        return processData(fileName = file.name, data = file.inputStream.use {
            file.size.toInt()
            it.readBytes()
        })
    }

    private fun checkContentSize(size: Long) {
        if (size >= MAX_DOC_SIZE) {
            throw IllegalArgumentException("File is too big size: $size, limit: $MAX_DOC_SIZE")
        }
    }

    private fun processData(fileName: String, data: ByteArray): WordsProcessResult {
        log.info("Processing filename {}", fileName)
        val start = System.currentTimeMillis()
        val store = docStorage.store(fileName, data)
        val wordCount = reader.read(store, consumer = { word -> repository.saveWord(word) })
        log.info("Extracted words: {} from filename {}", wordCount, fileName)
        return WordsProcessResult((System.currentTimeMillis() - start), fileName, wordCount)
    }

    data class WordsProcessResult(
            val processingTime: Long,
            val file: String,
            val wordCount: Int
    )

}
