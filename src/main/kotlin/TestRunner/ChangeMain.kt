package TestRunner

fun main() {

    testAround {
        runTest(BeforeAndAfterTwiceTestClass()) { println("Run test BeforeAndAfterTwiceTestClass") }
    }

    testAround {
        runTest(BeforeAndAfterTestClass()) { println("Run test BeforeAndAfterTestClass") }
    }

    testAround {
        runTest(AfterOnlyTestClass()) { println("Run test AfterOnlyTestClass") }
    }

    testAround {
        runTest(BeforeOnlyTestClass()) { println("Run test BeforeOnlyTestClass") }
    }

    testAround {
        runTest(EmptyTestClass()) { println("Run test EmptyTestClass") }
    }
}
