package uk.ac.cf.cs.nsa.cmt653.assignment.manager;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TodoManager implements TodoRepository {
    LinkedHashMap<String, Todo> dataStore = dataStore();

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
    public void remove(String todoName, int taskPosition) throws RuntimeException {
        int actualPosition = taskPosition - 1;
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException(todoName + "does not exist");
        }
        LinkedList<Task> tasks = dataStore.get(todoName).getTasks();
        try {
            Task theTask = tasks.get(actualPosition);
            if (tasks.remove(theTask)) {
                System.out.println("Task " + actualPosition + " removed!");
            }
        } catch (IndexOutOfBoundsException exception) {
            throw new IndexOutOfBoundsException("Task " + taskPosition + "does not exist");
        }
    }

    @Override
    public String checkTaskStatus(String todoName, int taskPosition) throws RuntimeException {
        int actualPosition = taskPosition - 1;
        String status = "";
        if (!dataStore.containsKey(todoName)) {
            throw new RuntimeException("Task with " + todoName + " does not exist");
        }
        try {
            status = dataStore.get(todoName)
                    .getTasks()
                    .get(actualPosition)
                    .getStatusString();
        } catch (IndexOutOfBoundsException exception) {
            throw new IndexOutOfBoundsException("Task " + taskPosition + "does not exist");
        }
        return status;
    }
}

