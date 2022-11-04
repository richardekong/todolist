package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedHashMap;

public interface TodoRepository {

    Todo createTodo(Todo todo);

    Todo viewTodo(String name);

    LinkedHashMap<String, Todo> listTodos();

    void append(String todoName, Task task);

    void remove(String todoName, int taskId);

    String checkTaskStatus(String todoName, int taskId);

}

