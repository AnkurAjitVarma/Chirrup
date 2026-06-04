package me.ankur_varma.chirrup.api.advice

import me.ankur_varma.chirrup.domain.exception.UserAlreadyExists
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthControllerAdvice {
    @ExceptionHandler(UserAlreadyExists::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun onUserAlreadyExists(e: UserAlreadyExists) =
        mapOf(
            "error" to mapOf(
                "code" to "USER_EXISTS",
                "message" to e.message,
            )
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onMethodArgumentNotValid(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = e.bindingResult.fieldErrors
            .groupBy(
                keySelector = { it.field },
                valueTransform = { it.defaultMessage }
            )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "error" to mapOf(
                        "code" to "INVALID",
                        "details" to errors
                    )
                )
            )

    }

}