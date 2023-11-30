package TestRunner

import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions

private const val BEFORE = "before"
private const val AFTER = "after"

class TestAround<T : Any> : TestRunner<T> {
    private lateinit var stepsMemberFunctions: Collection<KFunction<*>>
    private lateinit var steps: T

    override fun runTest(steps: T, test: () -> Unit) {
        println("START ${steps::class.simpleName}")
        this.steps = steps

        stepsMemberFunctions = steps::class.declaredMemberFunctions
        runAllBefore()
        test()
        runAllAfter()

        println("END ${steps::class.simpleName} \n")
    }

    private fun runAllBefore() =
        stepsMemberFunctions.filter { it.name.startsWith(BEFORE) }.forEach {
            print("call [$BEFORE] ${it.name}")
            it.call(steps)
        }

    private fun runAllAfter() =
        stepsMemberFunctions.filter { it.name.startsWith(AFTER) }.forEach {
            print("[$AFTER] ${it.name}")
            it.call(steps)
        }
}