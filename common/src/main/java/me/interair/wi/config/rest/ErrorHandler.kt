package me.interair.wi.config.rest

import com.google.common.base.Throwables
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ErrorHandler(
        @Value("\${spring.application.name:test}")
        private val name: String
): ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    protected fun handleIllegalArgumentException(ex: RuntimeException, request: WebRequest?): ResponseEntity<*>? {
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    /**
     * see https://github.com/spring-projects/spring-boot/issues/8625
     */
    @ExceptionHandler
    fun onError(request: HttpServletRequest, response: HttpServletResponse, throwable: Throwable): ResponseEntity<*> {
        val acceptText = isAcceptText(request)
        val headers = HttpHeaders()
        val body: Any = if (acceptText) {
            val date = LocalDateTime.now()
            val sw = StringWriter()
            PrintWriter(sw).use {
                it.print("$name $date\nError:\n")
                throwable.printStackTrace(it)
            }
            headers.contentType = MediaType.TEXT_PLAIN
            sw.toString()
        } else {
            val message = Throwables.getCausalChain(throwable).stream()
                    .map(Throwable::message)
                    //we need to use reduce without optional, for prevent NPE on null result from reducer
                    .reduce(null) { first, last -> last ?: first }
            val sw = StringWriter()
            PrintWriter(sw).use { throwable.printStackTrace(it) }
            UiError(
                    date = LocalDateTime.now(),
                    message = message,
                    stacktrace = sw.toString()
            )
        }
        val status = HttpStatus.valueOf(response.status)
        return ResponseEntity(body, headers, status)
    }

    private fun isAcceptText(request: HttpServletRequest): Boolean {
        return try {
            val accept = MimeTypeUtils.parseMimeTypes(request.getHeader("Accept"))
            accept.any { it.includes(MimeTypeUtils.TEXT_PLAIN) || it.includes(MimeTypeUtils.TEXT_HTML) }
        } catch (s: Exception) {
            // wrong header
            false
        }
    }
}

data class UiError(
        val date: LocalDateTime,
        val message: String?,
        val stacktrace: String
)
