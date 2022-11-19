package uk.ac.cf.cs.nsa.cmt653.assignment.manager;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;

import java.util.LinkedHashMap;
import java.util.LinkedList;

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
     * Saves a Todo item to the datastore object,
     * provided the item doesn't exist in the datastore.
     * param: Todo item to be saved
     * throws: RuntimeException a todo item with the same
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
     * Retrieves a Todo item from the dataStore
     * param: name of the Todo item to be retrieved
     * throws: RuntimeException if there is no Todo item
     * with such a name
     * returns: Todo item
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
     * Retrieves all Todo items from
     * the dataStore.
     * returns: List of Todo
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
     * Appends a task to the end of the Todo item,
     * as long as the Todo item exists.
     * params: name of the Todo item and
     * the task to be appended
     * */
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
     * Removes a Todo item from the dataStore,
     * using the name of the todo item, amd the position of
     * */
    @Override
    public void remove(String todoName, int taskPosition) throws RuntimeException {
        int actualPosition = taskPosition - 1;
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "\u0020does not exist");
        }
        LinkedList<Task> tasks = dataStore.get(todoName).getTasks();
        try {
            Task theTask = tasks.get(actualPosition);
            if (tasks.remove(theTask)) {
                System.out.println("Task" + taskPosition + "\u0020removed!");
            }
        } catch (IndexOutOfBoundsException exception) {
            throw new IndexOutOfBoundsException("Task" + taskPosition + "\u0020does not exist");
        }
    }

    @Override
    public String checkTaskStatus(String todoName, int taskPosition) throws RuntimeException {
        int actualPosition = taskPosition - 1;
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "\u0020does not exist");
        }
        try {
            return dataStore.get(todoName)
                    .getTasks()
                    .get(actualPosition)
                    .getStatus();
        } catch (IndexOutOfBoundsException exception) {
            throw new IndexOutOfBoundsException("Task" + taskPosition + "\u0020does not exist");
        }
    }
}

