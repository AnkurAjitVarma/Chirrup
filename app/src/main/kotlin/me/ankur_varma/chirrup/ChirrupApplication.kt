package me.ankur_varma.chirrup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChirrupApplication

fun main(args: Array<String>) {
	runApplication<ChirrupApplication>(*args)
}
