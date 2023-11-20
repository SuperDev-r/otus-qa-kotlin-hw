package example.dsl

import example.runner.TestAround

/**
 * Здесь определяется функция DSL  testAround
 * TestAround и есть основной окружающий класс
 * а initializer передаваемая ему функция- в этом примере println("§ invoke ... RUNNING")
 * по правилам DSL она выносится за скобки при вызове в лямбду
 * Затем она используется в Main.kt
 *
 */
fun <T : Any> testAround(initializer: TestAround<T>.() -> Unit): TestAround<T> = TestAround<T>().also{ it.initializer() }
/*{  // это то же самое длиннее, но понятнее
    val testAround = TestAround<T>()
    testAround.initializer()
    return testAround
}*/

fun <T : Any> testGroup(func: TestAround<T>.() -> Unit): TestAround<T> = TestAround<T>().apply(func)