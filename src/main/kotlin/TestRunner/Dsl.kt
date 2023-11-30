package TestRunner

fun <T : Any> testAround(initializer: TestAround<T>.() -> Unit): TestAround<T> =
    TestAround<T>().also { it.initializer() }