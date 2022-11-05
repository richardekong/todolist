package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedList;

public interface TodoRepository {

    void saveTodo(Todo todo);

    Todo findTodoByName(String name);

    LinkedList<Todo> listTodos();

    void appendTaskToEndOfTodo(String todoName, Task task);

    void remove(String todoName, int taskId);

    String checkTaskStatus(String todoName, int taskId);

}

