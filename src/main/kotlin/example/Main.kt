package example

import example.dsl.testAround
import example.tests.*

/**
 * В main применен вызов DSL функции testAround с вынесенной за скобки в лямбду функцией
 * функция testAround определена нами заранее в Dsl.kt
 * testAround сначала создает экземпляр класса TestAround и затем вызывает функцию из лямбды
 * Окружив контекстом Класса TestAround вызываем его функцию runTest
 * передавая ей анализируемый на before after класс
 */
fun main() {
    println("### start main ### \n ")
// вызов через DSL функцию

    testAround {
        runTest(BeforeTwiceAndAfterTwiceTestClass()) { println("§ invoke test BeforeTwiceAndAfterTwiceTestClass RUNNING") }
    }

    testAround {
        runTest(BeforeAndAfterTestClass()) { println("§ invoke test BeforeAndAfterTestClass RUNNING") }
    }

    testAround {
        runTest(AfterOnlyTestClass()) { println("§ invoke test AfterOnlyTestClass RUNNING") }
    }

    testAround {
        runTest(BeforeOnlyTestClass()) { println("§ invoke test BeforeOnlyTestClass RUNNING") }
    }

    testAround {
        runTest(EmptyTestClass()) { println("§ invoke test EmptyTestClass RUNNING") }
    }

    println("### finish main ###")
}