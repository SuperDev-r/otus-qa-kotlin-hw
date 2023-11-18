import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.full.declaredFunctions

interface TestRunner {
    fun <T> runTest(steps: T, test: () -> Unit)
}

 open class OverrideFun : TestRunner {
    override fun <T> runTest(steps: T, test: () -> Unit) {
    }
}

open class NewClass : OverrideFun() {
    fun runTest(steps: Steps, test: () -> Unit) {

        val myClass = Steps()
        val result = myClass::class.declaredFunctions.first { it.name.contains("before") }.call(myClass)
        val result1 = myClass::class.declaredFunctions.first { it.name.contains("after") }.call(myClass)

        /*val jjj = runTest(steps = myClass::class.declaredFunctions.first { it.name.contains("after") }.call(myClass)) {
            val result = myClass::class.declaredFunctions.first { it.name.contains("before") }.call(myClass)
            val result1 = myClass::class.declaredFunctions.first { it.name.contains("after") }.call(myClass)
            when (a + b) {
                4 -> println("Test success finished")
                5 -> println("Test fail finished")
            }

        }*/

        fun exampleTest() =
            runTest(steps = result) {
                val a = Random.nextInt(1..2)
                val b = Random.nextInt(1..2)
                when (a + b) {
                    4 -> println("Test success finished")
                    5 -> println("Test fail finished")
                }

            }

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
    val result = myClass::class.declaredFunctions.first { it.name.contains("before") }.call(myClass)
    val startTest = NewClass().runTest(steps = result) {
        val a = Random.nextInt(1..2)
        val b = Random.nextInt(1..2)
        when (a + b) {
            4 -> println("Test success finished")
            3, 2 -> println("Test fail finished")
        }
    }
    // val steps = object : Steps(){}
    // val myClass = Steps()
    //val steps = myClass::class.declaredFunctions.forEach(::println)
    // val steps = startTest::class.declaredFunctions.forEach(::println)


    // val myClass = MyClass()
    //  val result = myClass::class.declaredFunctions.first { it.name.contains("before") }.call(myClass)
    // val result1 = myClass::class.declaredFunctions.first { it.name.contains("after") }.call(myClass)


    //@Test
    /*  fun exampleTest() =
          startTest.runTest(steps = myClass::class.declaredFunctions.first { it.name.contains("after") }.call(myClass)) {
              val a = Random.nextInt(1..2)
              val b = Random.nextInt(1..2)
              when (a + b) {
                  4 -> println("Test success finished")
                  5 -> println("Test fail finished")
              }

          }

      *//*println(result)
    println(result1)*//*
*/
}
