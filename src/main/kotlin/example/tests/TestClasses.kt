package example.tests


class BeforeTwiceAndAfterTwiceTestClass {
    fun beforeFirst() {
        "before first".log()
    }


    fun afterFirst() {
        "after first".log()
    }

    fun beforeSecond() {

        "before second".log()
    }

    fun testTwiceTestClass1 () {

        "testTwiceTestClass_1".log()
    }

    fun afterSecond() {
        "after second".log()
    }
}

class BeforeAndAfterTestClass {
    fun beforeAlone() {
        "before".log()
    }

    fun afterAlone() {
        "after".log()
    }
}

class AfterOnlyTestClass {
    fun afterOnly() {
        "after".log()
    }
}

class BeforeOnlyTestClass {
    fun beforeOnly() {
        "before".log()
    }
}

class EmptyTestClass

// функция расширения для String с именем .log приименяется см выше
fun String.log() {
    println("-> $this running...")
}