package uk.ac.cf.cs.nsa.cmt653.assignment.manager;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoDataStore;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TodoManager implements TodoRepository {
    LinkedHashMap<String, Todo> dataStore = TodoDataStore.todoDataStore;

    @Override
    public void saveTodo(Todo todo) throws RuntimeException {
        if (dataStore.containsKey(todo.getName())) {
            throw new RuntimeException(todo.getName() + " already exist");
        }
        dataStore.put(todo.getName(), todo);
        System.out.println(todo.getName() + " saved");
    }

    @Override
    public Todo findTodoByName(String todoName) throws RuntimeException {
        Todo todo = dataStore.get(todoName);
        if (todo == null) {
            throw new RuntimeException(todoName + "does not exist.");
        }
        return todo;
    }

    @Override
    public LinkedList<String> listNamesOfTodos() {
        return new LinkedList<>(dataStore
                .values()
                .stream()
                .map(Todo::getName)
                .toList());
    }

    @Override
    public void appendTaskToEndOfTodo(String todoName, Task task) throws RuntimeException {
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "does not exist");
        }
        dataStore.get(todoName)
                .getTasks()
                .add(task);
    }

    @Override
    public void remove(String todoName, int taskId) throws RuntimeException {
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "does not exist");
        }

        LinkedList<Task> tasks = dataStore.get(todoName)
                .getTasks();

        Task theTask = tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findAny()
                .orElse(new Task());

        if (!tasks.contains(theTask)) {
            throw new RuntimeException("Task with " + taskId + " does not exist");
        }

        if (tasks.remove(theTask)) {
            System.out.println("Task with " + taskId + " removed!");
        }
    }

    @Override
    public String checkTaskStatus(String todoName, int taskId) throws RuntimeException {

        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException("Task with " + todoName + " does not exist");
        }

        return dataStore.get(todoName)
                .getTasks()
                .stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .map(Task::getStatusString)
                .orElse("");
    }
}

