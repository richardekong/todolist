package uk.ac.cf.cs.nsa.cmt653.assignment;

import uk.ac.cf.cs.nsa.cmt653.assignment.manager.TodoManager;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;
import uk.ac.cf.cs.nsa.cmt653.assignment.util.Command;

public class CommandLineRunner {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
        TodoRepository repo = new TodoManager();
        Command.listAllTodoNames.setExecutionBlock(repo::listTodos);
    }
}