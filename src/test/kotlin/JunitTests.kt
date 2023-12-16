import data.Priority
import data.Task
import data.TasksRepositoryMemory
import helpers.Step
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("Сьют проверки работы с тасками")
class JunitTests : Step() {
    private val FIRST_TASK_NAME = "Do some cofe"
    private val SECOND_TASK_NAME = "Do some meal"

    private val repository = TasksRepositoryMemory()

    private val taskFirst = Task(name = FIRST_TASK_NAME, priority = Priority.LOW, completed = false)
    private val taskSecond = Task(name = SECOND_TASK_NAME, priority = Priority.HIGH, completed = false)

    @DisplayName("Добавление таски в очередь и проверка - junit")
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
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().priority, Priority.LOW)
                        }, {
                            Assertions.assertEquals(first().completed, false)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Завершаем задачу и проверяем корректность работы фильтра по завершенным задачам - junit")
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
                            Assertions.assertEquals(size, 2)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(last().name, SECOND_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, false)
                        }, {
                            Assertions.assertEquals(last().completed, false)
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
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, SECOND_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, false)
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
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, true)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Удаление задачи - junit")
    @Test
    fun deleteTask() {
        with(repository) {
            lateinit var getAllUncompletedTasks: List<Task>
            step("Add two tasks and display the result") {

                addTask(taskFirst)
                addTask(taskSecond)

                getAllUncompletedTasks = getTasks(false)
                getAllUncompletedTasks.forEach(::println)

                Assertions.assertEquals(getAllUncompletedTasks.size, 2)
            }

            step("Delete one task and check the result") {
                deleteTask(getAllUncompletedTasks.first().id!!)

                val getAllTasks = getTasks(true)
                getAllTasks.forEach(::println)

                with(getAllTasks) {
                    assertAll(
                        {
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, SECOND_TASK_NAME)
                        }
                    )
                }
            }
        }
    }

    @DisplayName("Возвращаем таску в статус незавершенной - junit")
    @Test
    fun changeCompeteStateTaskAndCheckResult() {
        with(repository) {
            step("Add one task and check the result") {
                addTask(taskFirst)

                with(getTasks(true)) {
                    assertAll(
                        {
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, false)
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
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, true)
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
                            Assertions.assertEquals(size, 1)
                        }, {
                            Assertions.assertEquals(first().name, FIRST_TASK_NAME)
                        }, {
                            Assertions.assertEquals(first().completed, false)
                        }
                    )
                }
            }
        }
    }
}