package me.interair.wi.gw.word

import me.interair.wi.gw.partitioning.PartitionResolver
import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import me.interair.wi.word.WordsRepository
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.retry.Retry

class RestWordsRepository(
        val balancedClient: WebClient,
        val clientBuilder: WebClient.Builder,
        val partitionResolver: PartitionResolver,
        val discoveryClient: DiscoveryClient) : WordsRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByDocumentWord(word: String): Mono<WordReport> {
        return balancedClient.get().uri("http://${partitionResolver.resolvePartition(word)}/word/${word}")
                .retrieve()
                .bodyToMono(WordReport::class.java)
                .retryWhen(Retry.anyOf<Exception>(Exception::class.java)
                        .retryMax(3))
    }

    /**
     * replace to mq like kafka or use M/S
     */
    override fun saveWord(wd: WordData) {
        val instances = discoveryClient.getInstances(partitionResolver.resolvePartition(wd.word))
        Flux.fromIterable(instances)
                .parallel()
                .runOn(Schedulers.parallel())
                .doOnNext { i: ServiceInstance ->
                    clientBuilder
                            .baseUrl("http://${i.host}:${i.port}")
                            .build().put().uri("/word")
                            .bodyValue(wd)
                            .exchange()
                            .subscribe { body -> log.info("Send `{}` to `{}`:(`{}`), result: {}", wd.word, i.instanceId, i.host, body.statusCode())}
                }
                .sequential()
                .subscribe()

    }

}
