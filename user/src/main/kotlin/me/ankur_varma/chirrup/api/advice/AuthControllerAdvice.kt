package me.ankur_varma.chirrup.api.advice

import me.ankur_varma.chirrup.domain.exception.*
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

    @ExceptionHandler(UsernameTaken::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun onUsernameTaken(e: UsernameTaken) =
        mapOf(
            "error" to mapOf(
                "code" to "USER_EXISTS",
                "message" to e.message,
            )
        )

    @ExceptionHandler(UserDoesNotExist::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onUserDoesNotExist(e: UserDoesNotExist) = mapOf<String, String>()

    @ExceptionHandler(IncorrectPassword::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onIncorrectPassword(e: IncorrectPassword) = mapOf<String, String>()

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onInvalidTokenException(e: InvalidTokenException) =
        mapOf(
            "code" to "INVALID_TOKEN",
            "message" to e.message
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