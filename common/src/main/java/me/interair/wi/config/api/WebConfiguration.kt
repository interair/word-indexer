package me.interair.wi.config.api

import me.interair.wi.config.JacksonConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.*
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.retry.Backoff
import reactor.retry.Retry
import java.time.Duration

@Import(
        JacksonConfig::class,
        HttpMessageConvertersAutoConfiguration::class,
        DispatcherServletAutoConfiguration::class,
        ServletWebServerFactoryAutoConfiguration::class,
        ErrorMvcAutoConfiguration::class,
        HttpEncodingAutoConfiguration::class,
        MultipartAutoConfiguration::class,
        WebMvcAutoConfiguration::class,
        SwaggerConfiguration::class
)
@ComponentScan(basePackageClasses = [WebConfiguration::class])
@Configuration
class WebConfiguration {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter? {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setAfterMessagePrefix("Request data : ")
        return filter
    }

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter { request, next ->
                    next.exchange(request)
                            .retryWhen(Retry.anyOf<Exception>(Exception::class.java)
                                    .backoff(Backoff.fixed(Duration.ofMillis(100))).retryMax(3))
                }
                .filters { exchangeFilterFunctions ->
                    exchangeFilterFunctions.add(logRequest())
                    exchangeFilterFunctions.add(logResponse())
                }

    }

    private fun logRequest() : ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url())
            Mono.just(clientRequest)
        }
    }

    private fun logResponse() : ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { response ->
            log.debug("Reponse: {}", response.statusCode())
            Mono.just(response)
        }
    }
}