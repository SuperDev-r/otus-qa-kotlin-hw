package example

/**
 * Дан интерфейс interface TestRunner { fun  runTest(steps: T, test: () -> Unit) }.
 * Класс типа Т, передаваемый в steps: T,
 * содержит методы before* /  after*,
 * которые задают предусловия/чистят данные перед/после теста.
 *
 * class TestAround<T : Any> : TestRunner<T> реализует  Этот интерфейс
 * и (пере)определяет  override fun runTest(steps: T, test: () -> Unit)
 * Затем этот runTest будет вызываться из main с конкретными параметрами
 */
interface TestRunner<T> {
    fun runTest(steps: T, test: () -> Unit)
}


