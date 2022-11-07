package uk.ac.cf.cs.nsa.cmt653.assignment;

import uk.ac.cf.cs.nsa.cmt653.assignment.manager.TodoManager;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;
import uk.ac.cf.cs.nsa.cmt653.assignment.util.Command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static uk.ac.cf.cs.nsa.cmt653.assignment.util.Constant.*;

public class CommandLineRunner {

    public static void main(String[] args) {
        TodoRepository repo = new TodoManager();
        Scanner input = new Scanner(System.in);
        AtomicBoolean hasNotQuit = new AtomicBoolean(true);
        printTitle();
        showHelpDetails();
        System.out.println("Type a command ...");
        do {
            String commandPhrase = input.nextLine();
            switch (commandPhrase) {
                case LIST_ALL_TODO_NAMES -> listNamesOfTodos(repo);
                case VIEW_TODO_LIST -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    viewTodoListByTodoName(repo, todoName, input);
                }
                case APPEND_TASK -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    appendTaskToTodo(repo, todoName, input);
                }
                case REMOVE_TASK -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    removeTaskFromTodo(repo, todoName, input);
                }
                case CREATE_NEW_TODO -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    Todo todo = new Todo(todoName, new LinkedList<>());
                    createNewTodo(repo, todo, input);
                }
                case RECORD_TASK_COMPLETION -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    checkTaskStatus(repo, todoName, input);
                }
                case HELP -> showHelpDetails();

                case QUIT -> System.exit(0);

            }


        } while (hasNotQuit.get());

    }

    private static void printTitle() {
        printStars();
        System.out.print("*\t\t\t\t\tTODOLIST COMMANDLINE ASSIGNMENT\t\t\t\t\t*");
        printStars();
    }

    private static void showHelpDetails() {
        Command.HELP.executionBlock(() -> {
            printStars();
            System.out.print("*\t\t\t\t\tTODOLIST HELP SECTION\t\t\t\t\t\t\t*\n");
            printStars();
            Arrays.stream(Command.values()).forEach(cmd -> System.out.printf("\t\tType\s\"%s\"\sto\s%s\t\t\t\t\t\t\t\n",
                    cmd.instruction(), cmd.tip()));
            printStars();

        });
    }

    private static void listNamesOfTodos(TodoRepository repository) {
        AtomicInteger index = new AtomicInteger(1);
        printStars();
        repository.listNamesOfTodos().forEach(todoName -> System.out.printf("\t%d.\s%s\n", index.getAndIncrement(), todoName));
        printStars();
    }

    private static void viewTodoListByTodoName(TodoRepository repository, String todoName, Scanner input) {
        printStars();
        try {
            Todo todoToBeViewed = repository.findTodoByName(todoName);
            AtomicInteger counter = new AtomicInteger(1);
            System.out.println("Todo Name:\u0020" + todoToBeViewed.getName());
            printStars();
            todoToBeViewed.getTasks().forEach(task -> System.out.printf("\n%d.\s%s|\s%d\sminutes|\s%s\n",
                    counter.getAndIncrement(),
                    task.getDescription(),
                    task.getDeadlineInMinutes().getSeconds() / 60,
                    task.getStatusString()));
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            String reply = input.nextLine();
            switch (reply) {
                case Y -> {
                    System.out.println("What's the Todo name again?");
                    todoName = input.nextLine();
                    viewTodoListByTodoName(repository, todoName, input);
                }
                case N -> System.exit(0);
            }
        }
        printStars();
    }

    private static void appendTaskToTodo(TodoRepository repository, String todoName, Scanner input){
        printStars();
        try{
           System.out.println("What is the task description?");
           String description = input.nextLine();
           System.out.println("What is the task deadline in minutes");
           Long deadlineInMinutes = Long.parseLong(input.nextLine());
           Task taskToAppend = new Task(description, deadlineInMinutes);
           repository.appendTaskToEndOfTodo(todoName, taskToAppend);
        }catch (RuntimeException rte){
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            String reply = input.nextLine();
            switch (reply){
                case Y -> {
                    System.out.println("What's the Todo name again?");
                    todoName = input.nextLine();
                    appendTaskToTodo(repository, todoName, input);
                }
                case N -> System.exit(0);
            }
        }
        printStars();
    }

    private static void removeTaskFromTodo(TodoRepository repository, String todoName, Scanner input){
        printStars();
        try{
            System.out.println("What's the position of this task?" );
            int taskPosition = Integer.parseInt(input.nextLine());
            repository.remove(todoName, taskPosition);
        }catch (RuntimeException rte){
            System.err.println(rte.getMessage()+"\u0020Do you wish to continue?[Y/N]");
            switch (input.nextLine()){
                case Y -> {
                    System.out.println("What's the todo name again?");
                    todoName = input.nextLine();
                    removeTaskFromTodo(repository, todoName, input);
                }
                case N -> System.exit(0);
            }
        }
    }

    private static void createNewTodo(TodoRepository repository, Todo todo, Scanner input){
        printStars();
        System.out.println("What's the task description?");
        String description = input.nextLine();
        System.out.println("What's the task deadline in minutes?");
        Long deadlineInMinutes = Long.parseLong(input.nextLine());
        Task task = new Task(description, deadlineInMinutes);
        boolean taskAdded = todo.getTasks().add(task);
        if (taskAdded) {
            System.out.println("Task added!");
        }
        System.out.println("Do you want to add another task?[Y/N]");
        switch (input.nextLine()){
            case Y -> createNewTodo(repository, todo, input);
            case N -> {
                try {
                    repository.saveTodo(todo);
                }catch (RuntimeException rte){
                    System.err.println(rte.getMessage()+"\u0020Try again with a different todo name.");
                }
            }
        }
    }

    private static void checkTaskStatus(TodoRepository repository, String todoName, Scanner input){
        printStars();
        try{
            System.out.println("What's the position of this task?");
            int taskPosition = Integer.parseInt(input.nextLine());
            String status = repository.checkTaskStatus(todoName, taskPosition);
            System.out.printf("\nTask\sis\s%s\n", status);
        }catch(RuntimeException rte){
            System.err.println(rte.getMessage()+"\u0020Do you wish to continue?[Y/N]");
            switch (input.nextLine()){
                case Y -> {
                    System.out.println("What's the todo name again?");
                    todoName = input.nextLine();
                    checkTaskStatus(repository, todoName, input);
                }
                case N -> System.exit(0);
            }
        }
    }

    private static void printStars() {
        System.out.print("\n*********************************************************************\n");
    }
}

