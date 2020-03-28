package me.interair.wi.config.api

import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 */
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun swaggerDocket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ApiInfoBuilder().title("Word Indexer")
                        .version("1")
                        .description("FT (CA) Word Indexer").build())
                .select()
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
    }

}