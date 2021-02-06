package ru.krivonos.remindercalendar.general.hello.world

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun main() {
    println("Hello, World!")
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        println(i)
        delay(300)
        emit(i)
    }
}

fun investigation() = runBlocking<Unit>((Dispatchers.Default)) {
    launch {
        while (true) {
            log("a")
            delay(1000)
        }
    }
    launch {
        while (true) {
            log("b")
            delay(1500)
        }
    }
    withContext(Dispatchers.IO){
        delay(500)
        log("Sleeping")
        Thread.sleep(10000000)
    }
    launch {
        log("c")
        delay(1500)
    }
}
