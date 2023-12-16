package helpers

import io.qameta.allure.Allure.*

open class Step {

    fun <O> step(title: String, block: () -> O): O {
        val blockToExecute = ThrowableRunnable { block() }
        println("Do step: $title")
        return step(title, blockToExecute)
    }
}