package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface TodoRepository {

    void saveTodo(Todo todo);

    Todo findTodoByName(String name);

    LinkedList<String> listNamesOfTodos();

    void appendTaskToEndOfTodo(String todoName, Task task);

    void remove(String todoName, int taskPosition);

    String checkTaskStatus(String todoName, int taskPosition);

    default LinkedHashMap<String, Todo> dataStore() {
        return TodoDataStore.INSTANCE.getDataStore();
    }

}

