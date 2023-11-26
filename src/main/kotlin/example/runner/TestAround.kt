package example.runner

import example.TestRunner
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberFunctions

/**
 * Константы с подстрокой, определяющей методы в имени которых BEFORE и AFTER
 */
private const val BEFORE = "before"
private const val AFTER = "after"
/**
 *  Создайте класс реализующий интерфейс TestRunner<T>
 */
class TestAround<T : Any> : TestRunner<T> {
    // Вся коллекция тестов, вместе с BEFORE и AFTER
    private lateinit var stepsMemberFunctions: Collection<KFunction<*>>
    // <T> присваивается при вызове fun runTest из его параметров, переданных при вызове fun из main
    private lateinit var steps: T  // Generic приходит - декларируется от  создателя класса и идет в интерфейс
/**
 * Внутри класса переопределите метод runTest
 * Сделанный и настроенный  runTest вызывается из main:
 * runTest(BeforeTwiceAndAfterTwiceTestClass()) { println("§ BeforeTwiceAndAfterTwiceTestClass RUNNING") }
 * и передается класс в котором сидят запускаемые функции тестов (в т.ч. м.б. before* и after*):
 * BeforeTwiceAndAfterTwiceTestClass(), BeforeAndAfterTestClass, AfterOnlyTestClass, BeforeOnlyTestClass, EmptyTestClass
 * на место функции test сюда main передает println("§...")
 */
    override fun runTest(steps: T, test: () -> Unit) {
    println(">>> STARTing ${steps::class.simpleName}")
    // укладываем Класс, который обрабатывать на запуск before*, и after*
        this.steps = steps
        // Returns non-extension non-static functions declared in this class steps: T
        // Возвращает нестатические функции без расширения, объявленные в этом классе steps: T
        //    val clazz = steps::class.java.kotlin      // работает
        //    val instance = clazz.createInstance()
        stepsMemberFunctions = steps::class.declaredMemberFunctions
    //println(stepsMemberFunctions)
    /**
     * Внутри этого метода необходимо сначала вызвать методы с before* из steps,
     * далее прогнать тест (запустить передаваемую функцию test),
     * и после вызвать методы с after* из steps.
     *
     */
        runAllBefore()
        test.invoke()  // вызывается тестовая функция ( в данном случае println("§ ...RUNNING")) можно просто test()
        runAllAfter()

    println("<<< ENDing   ${steps::class.simpleName} \n")
    }

    // сначала вызвать методы с before* из steps,
    private fun runAllBefore() =
        stepsMemberFunctions.filter { it.name.startsWith(BEFORE) }.forEach {
            print("call [$BEFORE] ${it.name} --")
            it.call(steps)  // вызов it - функции из класса начинающейся с before (steps??)  steps.run { it }
            // steps::class.java.getMethod(it.name).invoke(steps)
        }

    // после вызвать методы с after* из steps.
    private fun runAllAfter() =
        stepsMemberFunctions.filter { it.name.startsWith(AFTER) }.forEach {
            print("[$AFTER] ${it.name} --")
            it.call(steps)  // вызов it - функции из класса начинающейся с after (steps??)  steps.run { it }
            // steps::class.java.getMethod(it.name).invoke(steps)
            /**
             * Вызывает этот вызываемый объект с указанным списком аргументов и возвращает результат.
             * Выдает исключение, если количество указанных аргументов не равно размеру параметров или если их типы не совпадают с типами параметров.
             */
        }

    //  steps.runStep("before")
    fun Any.runStep(stepName: String) {
        this::class.members
                .filter { it.name.contains(stepName) }
                .forEach {
                    println("Start step: ${it.name}")
                    it.call(this)
                    println("End step: ${it.name}")
                }
    }
    /**
     * Функция расширения для Generic вызывает и before и after
     */
    private fun T.beforeAfter(beforeAfter: String) = this::class.declaredFunctions.filter { kFunction -> kFunction.name.contains(beforeAfter) }
            .forEach { it.call(this) }

}


