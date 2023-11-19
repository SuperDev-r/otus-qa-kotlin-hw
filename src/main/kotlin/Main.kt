import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions

interface TestRunner {
    fun <T> runTest(steps: T, test: () -> Unit)
}

open class RunTestClass : TestRunner {
    override fun <T> runTest(steps: T, test: () -> Unit) {
        steps!!::class.declaredFunctions.filter { it.name.startsWith("before") }.forEach {
            invokeWrapper(steps, it)
        }
        test.invoke()
        steps!!::class.declaredFunctions.filter { it.name.startsWith("after") }.forEach {
            invokeWrapper(steps, it)
        }
    }

    private fun <T> invokeWrapper(source: T, testFunc: KFunction<*>) {
        println("Before invoke function")
        testFunc.call(source)
        println("After invoke function")
    }
}

open class Steps {
    fun beforeTest() {
        println("Prepare data before test")
    }

    fun afterSuccessTest() {
        println("Clear data after success test")
    }

    fun doIfFailTest() {
        println("Do something if fail test")
    }
}

fun main() {
    val myClass = Steps()
    RunTestClass().runTest(steps = myClass) {
        val a = Random.nextInt(1..2)
        val b = Random.nextInt(1..2)
        when (a + b) {
            4 -> println("Test success finished")
            3, 2 -> println("Test fail finished")
        }
    }
}
