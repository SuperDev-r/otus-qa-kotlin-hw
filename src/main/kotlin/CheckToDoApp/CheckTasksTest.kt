package CheckToDoApp

import data.Priority.*
import data.Task
import data.TasksRepositoryMemory
import io.qameta.allure.Allure.step
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("Сьют проверки работы с тасками")
class CheckTasksTest : Step() {

    private val FIRST_TASK_NAME = "Do some cofe"
    private val SECOND_TASK_NAME = "Do some meal"

    private val repository = TasksRepositoryMemory()

    private val taskFirst = Task(name = FIRST_TASK_NAME, priority = LOW, completed = false)
    private val taskSecond = Task(name = SECOND_TASK_NAME, priority = HIGH, completed = false)

    @DisplayName("Добавление таски в очередь и проверка")
    @Test
    fun addAndCheckTask() {
        with(repository) {
            addTask(taskFirst)
            step("Adding task: $FIRST_TASK_NAME.") {

                val getAllUncompletedTasks = getTasks(false)
                println(getAllUncompletedTasks.first())

                with(getAllUncompletedTasks) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(first().priority, LOW)
                        }, {
                            assertEquals(first().completed, false)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Завершаем задачу и проверяем корректность работы фильтра по завершенным задачам")
    @Test
    fun competeTaskAndCheckResult() {
        with(repository) {
            lateinit var getAllUncompletedTasks: List<Task>
            step("Add two tasks and display the result") {
                addTask(taskFirst)
                addTask(taskSecond)

                getAllUncompletedTasks = getTasks(false)
                getAllUncompletedTasks.forEach(::println)

                with(getAllUncompletedTasks) {
                    assertAll(
                        {
                            assertEquals(size, 2)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(last().name, SECOND_TASK_NAME)
                        }, {
                            assertEquals(first().completed, false)
                        }, {
                            assertEquals(last().completed, false)
                        }
                    )
                }
            }

            step("Complete one task and display the result") {
                completeTask(getAllUncompletedTasks.first().id!!)
                val getUncompletedTasks = getTasks(false)
                getUncompletedTasks.forEach(::println)

                with(getUncompletedTasks) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, SECOND_TASK_NAME)
                        }, {
                            assertEquals(first().completed, false)
                        }
                    )
                }
            }

            step("Check all task and display the result") {
                val getAllTasks = getTasks(true)
                getAllTasks.forEach(::println)

                with(getAllTasks.filter { it.completed }) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(first().completed, true)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Удаление задачи")
    @Test
    fun deleteTask() {
        with(repository) {
            lateinit var getAllUncompletedTasks: List<Task>
            step("Add two tasks and display the result") {

                addTask(taskFirst)
                addTask(taskSecond)

                getAllUncompletedTasks = getTasks(false)
                getAllUncompletedTasks.forEach(::println)

                assertEquals(getAllUncompletedTasks.size, 2)
            }

            step("Delete one task and check the result") {
                deleteTask(getAllUncompletedTasks.first().id!!)

                val getAllTasks = getTasks(true)
                getAllTasks.forEach(::println)

                with(getAllTasks) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, SECOND_TASK_NAME)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Возвращаем таску в статус незавершенной")
    @Test
    fun changeCompeteStateTaskAndCheckResult() {
        with(repository) {
            step("Add one task and check the result") {
                addTask(taskFirst)

                with(getTasks(true)) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(first().completed, false)
                        }
                    )
                }
            }

            step("Complete task and check the result") {
                completeTask(getTasks(true).first().id!!)
                val getCompletedTasks = getTasks(true)
                getCompletedTasks.forEach(::println)

                with(getCompletedTasks) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(first().completed, true)
                        }
                    )
                }
            }

            step("Change task state and display the result") {
                val getAllTasks = getTasks(true)
                getAllTasks.forEach(::println)

                uncompleteTask(getAllTasks.first().id!!)
                val getUncompletedTasks = getTasks(false)

                with(getUncompletedTasks) {
                    assertAll(
                        {
                            assertEquals(size, 1)
                        }, {
                            assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            assertEquals(first().completed, false)
                        }
                    )
                }
            }
        }
    }
}