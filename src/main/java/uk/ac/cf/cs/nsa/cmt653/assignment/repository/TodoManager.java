package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedHashMap;

public class TodoManager implements TodoRepository{

    @Override
    public Todo createTodo(Todo todo) {
        return null;
    }

    @Override
    public Todo viewTodo(String name) {
        return null;
    }

    @Override
    public LinkedHashMap<String, Todo> listTodos() {
        return null;
    }

    @Override
    public void append(String todoName, Task task) {

    }

    @Override
    public void remove(String todoName, int taskId) {

    }

    @Override
    public String checkTaskStatus(String todoName, int taskId) {
        return null;
    }
}
