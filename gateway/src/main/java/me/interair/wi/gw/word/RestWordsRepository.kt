package me.interair.wi.gw.word

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.springframework.web.client.RestTemplate

class RestWordsRepository(val balancedRestClient: RestTemplate, val partitionResolver: PartitionResolver) : WordsRepository {

    @HystrixCommand(groupKey = "words")
    override fun findByDocumentWord(word: String): WordReport {
        return balancedRestClient.getForObject("http://${partitionResolver.resolvePartition(word)}/word/${word}",
                WordReport::class.java)!!
    }

    /**
     * replace to mq like kafka or use M/S
     */
    @HystrixCommand(groupKey = "words")
    override fun saveWord(wordData: WordData) {
        balancedRestClient.postForLocation("http://${partitionResolver.resolvePartition(wordData.word)}/word", wordData)
    }
}