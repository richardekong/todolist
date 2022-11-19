package uk.ac.cf.cs.nsa.cmt653.assignment.manager;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Status;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * TodoManager implements the TodoRepository which has
 * total access to the dataStore object, an instance
 * of TodoDataStore.
 * As an implementor of the TodoRepository, TodoManager
 * has automatic access to the underlying TodoDataStore
 */
public class TodoManager implements TodoRepository {
    LinkedHashMap<String, Todo> dataStore = dataStore();

    /**
     * Saves a TodoList item to the datastore object,
     * provided the item doesn't exist in the datastore.
     * Param: TodoList item to be saved
     * Throws: RuntimeException when a TodoList item with the same name
     * exists in the dataStore.
     */
    @Override
    public void saveTodo(Todo todo) throws RuntimeException {
        if (dataStore.containsKey(todo.getName())) {
            throw new RuntimeException(todo.getName() + "\u0020already exist");
        }
        dataStore.put(todo.getName(), todo);
        System.out.println(todo.getName() + "\u0020saved!");
    }

    /**
     * Retrieves a TodoList item from the dataStore
     * Param: todoName - the name of the TodoList item to be retrieved
     * from the dataStore
     * Throws: RuntimeException if there is no TodoList item
     * with such a name
     * returns: TodoList item
     */
    @Override
    public Todo findTodoByName(String todoName) throws RuntimeException {
        Todo todo = dataStore.get(todoName);
        if (todo == null) {
            throw new RuntimeException(todoName + "\u0020does not exist.");
        }
        return todo;
    }

    /**
     * Retrieves all TodoList items from
     * the dataStore.
     * returns: List of TodoList
     */
    @Override
    public LinkedList<String> listNamesOfTodos() {
        return new LinkedList<>(dataStore
                .values()
                .stream()
                .map(Todo::getName)
                .toList());
    }

    /**
     * Appends a task to the end of the TodoList item,
     * as long as the TodoList item exists.
     * Params: todoName - name of the TodoList item and
     * the task to be appended.
     * task - The task to be appended to the end of the todoList
     * Throws: RuntimeException if the todoName parameter can't be mapped
     * with any of the TodoList items in the dataStore
     */
    @Override
    public void appendTaskToEndOfTodo(String todoName, Task task) throws RuntimeException {
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "\u0020does not exist");
        }
        boolean added = dataStore.get(todoName).add(task);
        if (added) {
            System.out.println("Task added!");
        }
    }

    /**
     * Removes a TodoList item from the dataStore,
     * using the name of the TodoList item, and task ID
     * Params: todoName - The name of the TodoList from which a given task is removed.
     * taskId - The id of the task to be removed.
     * Throws: RuntimeException or its subclass, if there are no TodoList
     * with the specified todoName, or if there is no task
     * with the specified taskId
     */
    @Override
    public void remove(String todoName, int taskId) throws RuntimeException {
        LinkedList<Task> tasks;
        if (!dataStore.containsKey(todoName)) {
            throw new NoSuchElementException(todoName + "\u0020does not exist");
        }
        tasks = dataStore.get(todoName).getTasks();
        tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findAny()
                .ifPresentOrElse(task -> {
                    if (tasks.remove(task)) {
                        System.out.printf("Removed Task with id number %d from %s\n", taskId, todoName);
                    }
                }, () -> {
                    throw new NoSuchElementException("Task not found");
                });
    }

    /**
     * Determines the status of Task
     * based on the associated TodoList's name
     * and the Task's id.
     * Params: todoName - The name of the TodoList from which a task's status is determined
     * taskId - The Task's ID which uniquely identifies the Task to be queried
     * Throws: RuntimeException or it's subclass when a TodoList with the specified todoName
     * is not found, or when a Task with the specified taskId is not found.
     * Return: String status of the Task which could be done or undone.
     */
    @Override
    public String checkTaskStatus(String todoName, int taskId) throws RuntimeException {

        if (!dataStore.containsKey(todoName)) {
            throw new NoSuchElementException(todoName + "\u0020does not exist");
        }
        return dataStore.get(todoName)
                .getTasks()
                .stream()
                .filter(task -> task.getId() == taskId)
                .findAny()
                .orElseThrow(() -> {
                    throw new NoSuchElementException(
                            String.format("Task with id %d does not exist", taskId)
                    );
                })
                .getStatus();
    }

    /**
     * Enable the Task completion from a TodoList to be recorded
     * as either undone or done.
     * Params: todoName - The name of the todoList from which Task completion will be recorded
     * taskId - The Task's ID which uniquely identifies the Task whose completion is being recorded
     * status - suggests if the Task is scheduled, undone, done or close. The status of a task can't
     * be changed if the task is closed or done after the deadline.
     * Throws: RuntimeException or it's subclass when a TodoList with the specified todoName
     * is not found, or when a Task with the specified taskId is not found
     */
    @Override
    public void recordTaskCompletion(String todoName, int taskId, Status status) throws RuntimeException {
        if (!dataStore.containsKey(todoName)) {
            throw new NoSuchElementException(todoName + "\u0020does not exist");
        }
        dataStore.get(todoName)
                .getTasks()
                .stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchElementException(
                            String.format("Task with id %d does not exist", taskId)
                    );
                })
                .setStatus(status);
    }
}

