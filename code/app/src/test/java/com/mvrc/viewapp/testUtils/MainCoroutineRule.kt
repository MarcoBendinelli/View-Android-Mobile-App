package com.mvrc.viewapp.testUtils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule for testing coroutines that need to run on the main dispatcher.
 * This rule configures the main dispatcher to use a provided test dispatcher during the test's execution.
 * After the test is finished, it resets the main dispatcher to its original state.
 *
 * @param dispatcher The test dispatcher to use for the main dispatcher. Defaults to [StandardTestDispatcher].
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    /**
     * Configures the main dispatcher to use the provided test dispatcher before starting the test.
     *
     * @param description The description of the test being executed.
     */
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    /**
     * Resets the main dispatcher to its original state after the test has finished.
     *
     * @param description The description of the test being executed.
     */
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}