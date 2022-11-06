package uk.ac.cf.cs.nsa.cmt653.assignment;

import uk.ac.cf.cs.nsa.cmt653.assignment.manager.TodoManager;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;
import uk.ac.cf.cs.nsa.cmt653.assignment.util.Command;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandLineRunner {
    public static void main(String[] args) {

        System.out.println("Application entry point");

        TodoRepository repo = new TodoManager();

        //List of todo names
        Command.LIST_ALL_TODO_NAMES.setExecutionBlock(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            System.out.println("=================================");
            System.out.println("List of Todo Names:");
            System.out.println("=================================");
            repo.listNamesOfTodos()
                    .forEach(name -> System.out.printf("%d\t%s\n", counter.incrementAndGet(), name));
        });

        //View a todo list
        Command.VIEW_TODO_LIST.setExecutionBlock(() -> {
            String todoName = "Lectures";
            Todo lectureTodo = repo.findTodoByName(todoName);
            System.out.println("=================================");
            System.out.printf("\s%s:\s\n", lectureTodo.getName());
            System.out.println("=================================");
            System.out.println("+\tTasks:\n");
            lectureTodo.getTasks().forEach(task ->{
                System.out.printf("\t+\s%s\t%d\smins\t%s\n",
                        task.getDescription(),
                        (task.getDeadlineInMinutes().getSeconds()/60),
                        task.getStatusString());
            });
        });

        //Add a task to a todo list
        Command.APPEND_TASK.setExecutionBlock(() ->{
            String todoName = "Lectures";
            Todo lectureTodo = repo.findTodoByName(todoName);
            lectureTodo.getTasks().add(new Task("Attend Cyber Security", 45L));
            System.out.println("=================================");
            System.out.printf("\s%s:\s\n", lectureTodo.getName());
            System.out.println("=================================");
            lectureTodo.getTasks().forEach(task ->{
                System.out.printf("\s+\s%s\t%d\sminutes\t%s\s+\n",
                        task.getDescription(),
                        (task.getDeadlineInMinutes().getSeconds()/60),
                        task.getStatusString());
            });
            System.out.print("\s++++++++++++++++++++++++++++++++++++++++++++++++\s");
        });

        //Remove a task from a todo list
        Command.REMOVE_TASK.setExecutionBlock(()->{
            Todo cookingTodo = repo.findTodoByName("Cooking");
            cookingTodo.getTasks().remove(4);
            System.out.println("=================================");
            System.out.printf("\s%s:\s\n", cookingTodo.getName());
            System.out.println("=================================");
            cookingTodo.getTasks().forEach(task ->{
                System.out.printf("\s+\s%s\t%d\sminutes\t%s\s+\n",
                        task.getDescription(),
                        (task.getDeadlineInMinutes().getSeconds()/60),
                        task.getStatusString());
            });
            System.out.print("\s++++++++++++++++++++++++++++++++++++++++++++++++\s");
        });

        //Create a new todo list with its tasks
        Command.CREATE_NEW_TODO.setExecutionBlock(()->{
            LinkedList<Task> tasks = new LinkedList<>(
                    List.of(
                            new Task("Attend Lectures", 40L),
                            new Task("Prepare for hackerthon", 50L),
                            new Task("Prepare for video interview with ARM",30L)
                    )
            );
            Todo mondayTodo = new Todo("Monday", tasks);
            repo.saveTodo(mondayTodo);
            AtomicInteger counter = new AtomicInteger(0);
            System.out.println("=================================");
            System.out.println("List of Todo Names:");
            System.out.println("=================================");
            repo.listNamesOfTodos()
                    .forEach(name -> System.out.printf("%d\t%s\n", counter.incrementAndGet(), name));
        });

        //check the status of a task
        Command.RECORD_TASK_COMPLETION.setExecutionBlock(() ->{
            System.out.println(repo.checkTaskStatus("Cooking", 4));
        });
    }
}