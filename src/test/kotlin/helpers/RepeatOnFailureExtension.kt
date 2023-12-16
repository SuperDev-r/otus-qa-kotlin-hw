package helpers

import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe

class RepeatOnFailureExtension : TestCaseExtension {

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        val repeatCount = 7
        var test = execute(testCase)
        lateinit var assertionError: AssertionError

        for (i in 1..repeatCount) {
            try {
                test.isErrorOrFailure shouldBe false
                return TestResult.Success(test.duration)
            } catch (e: AssertionError) {
                assertionError = e
                if (i == repeatCount) {
                    return TestResult.Failure(test.duration, assertionError)
                } else test = execute(testCase)
            }
        }
        return TestResult.Failure(test.duration, assertionError)
    }
}