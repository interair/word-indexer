package me.interair.wi.node

import me.interair.wi.word.Ref
import me.interair.wi.word.WordData
import me.interair.wi.word.WordReport
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NodeApplicationTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun storeWord() {
        restTemplate.put("/word", WordData("test", Ref("test.txt")))
        val report = restTemplate.getForObject("/word/test", WordReport::class.java)
        assertEquals(1, report.refs.size)
        assertEquals("test.txt", report.refs.iterator().next().id)

    }

}