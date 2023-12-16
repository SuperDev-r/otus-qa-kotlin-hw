import data.Priority.LOW
import data.Priority.HIGH
import data.Task
import data.TasksRepositoryMemory
import helpers.RepeatOnFailureExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

private val FIRST_TASK_NAME = "Do some cofe"
private val SECOND_TASK_NAME = "Do some meal"

private val repository = TasksRepositoryMemory()

private val taskFirst = Task(name = FIRST_TASK_NAME, priority = LOW, completed = false)
private val taskSecond = Task(name = SECOND_TASK_NAME, priority = HIGH, completed = false)

class TestsWithKotest : FunSpec({
    extension(RepeatOnFailureExtension())
    test("Добавление таски в очередь и проверка - kotest") {
        with(repository) {
            addTask(taskFirst)
            val getAllUncompletedTasks = getTasks(false)

            with(getAllUncompletedTasks) {
                size shouldBe 1
                getAllUncompletedTasks.first().name shouldBe taskFirst.name
            }
        }
    }

    test("Удаление задачи - kotest") {
        with(repository) {
            addTask(taskFirst)
            addTask(taskSecond)
            val getAllUncompletedTasks = getTasks(false)

            getAllUncompletedTasks.size shouldBe 2

            deleteTask(getAllUncompletedTasks.first().id!!)
            val getAllTasks = getTasks(true)

            with(getAllTasks) {
                size shouldBe 1
                first().name shouldBe taskSecond.name
            }
        }
    }

    test("Завершаем задачу и проверяем корректность работы фильтра - kotest") {
        with(repository) {
            addTask(taskFirst)
            addTask(taskSecond)
            val getAllUncompletedTasks = getTasks(false)

            with(getAllUncompletedTasks) {
                size shouldBe 2
                first().name shouldBe taskFirst.name
                last().name shouldBe taskSecond.name

                first().completed shouldNotBe true
                last().completed shouldNotBe true
            }

            completeTask(getAllUncompletedTasks.first().id!!)
            val getUncompletedTasks = getTasks(false)

            with(getUncompletedTasks) {
                size shouldBe 1
                first().name shouldBe taskSecond.name
                first().completed shouldNotBe true
            }
        }
    }

    test("Возвращаем таску в статус незавершенной - kotest") {
        with(repository) {
            addTask(taskFirst)
            val getAllUncompletedTasks = getTasks(false)

            with(getAllUncompletedTasks) {
                size shouldBe 1
                first().name shouldBe taskFirst.name
                first().completed shouldNotBe true
            }

            completeTask(getAllUncompletedTasks.first().id!!)
            val getCompletedTasks = getTasks(true)

            with(getCompletedTasks) {
                size shouldBe 1
                first().name shouldBe taskFirst.name
                first().completed shouldNotBe false
            }

            uncompleteTask(getCompletedTasks.first().id!!)
            val getUncompletedTasks = getTasks(false)

            with(getUncompletedTasks) {
                size shouldBe 1
                first().name shouldBe taskFirst.name
                first().completed shouldNotBe true
            }
        }
    }
})