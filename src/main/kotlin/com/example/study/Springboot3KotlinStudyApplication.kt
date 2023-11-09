package com.example.study

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class Springboot3KotlinStudyApplication

fun main(args: Array<String>) {
    runApplication<Springboot3KotlinStudyApplication>(*args)
}
