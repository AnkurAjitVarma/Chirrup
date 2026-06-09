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

    @ExceptionHandler(InvalidAuthToken::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onInvalidTokenException(e: InvalidAuthToken) =
        mapOf(
            "code" to "INVALID_TOKEN",
            "message" to e.message
        )

    @ExceptionHandler(UserNotVerified::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun onUserNotVerified(e: UserNotVerified) = mapOf<String, String>()

    @ExceptionHandler(ExpiredVerificationToken::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onExpiredVerificationToken(e: ExpiredVerificationToken) = mapOf(
        "error" to "verification_token_expired",
        "message" to "The verification link has expired. Please request a new one."
    )

    @ExceptionHandler(UsedVerificationToken::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onUsedVerificationToken(e: UsedVerificationToken) = mapOf(
        "error" to "verification_token_used",
        "message" to "The verification token has been used."
    )

    @ExceptionHandler(InvalidVerificationToken::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onInvalidVerificationToken(e: InvalidVerificationToken) = mapOf(
        "error" to "verification_token_invalid",
        "message" to "The verification token is invalid."
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