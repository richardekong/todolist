package uk.ac.cf.cs.nsa.cmt653.assignment;

import uk.ac.cf.cs.nsa.cmt653.assignment.manager.TodoManager;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;
import uk.ac.cf.cs.nsa.cmt653.assignment.util.Command;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandLineRunner {
    public static void main(String[] args) {

        TodoRepository repo = new TodoManager();

        //List of todo names
        Command.listAllTodoNames.setExecutionBlock(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            System.out.println("=================================");
            System.out.println("List of Todo Names:");
            System.out.println("=================================");
            repo.listNamesOfTodos()
                    .forEach(name -> System.out.printf("%d\t%s\n", counter.incrementAndGet(), name));
        });

        //View a todo list
        Command.viewATodoList.setExecutionBlock(() -> {
            String todoName = "Lectures";
            Todo cookingTodo = repo.findTodoByName(todoName);
            AtomicInteger counter = new AtomicInteger(0);
            System.out.println("=================================");
            System.out.printf("\s%s:\s\n", cookingTodo.getName());
            System.out.println("=================================");
            System.out.println("+\tTasks:\n");
            cookingTodo.getTasks().forEach(task ->{
                System.out.printf("\t+\s%s\t%d\smins\t%s\n",
                        task.getDescription(),
                        (task.getDeadlineInMinutes().getSeconds()/60),
                        task.getStatusString());
            });
        });
    }
}