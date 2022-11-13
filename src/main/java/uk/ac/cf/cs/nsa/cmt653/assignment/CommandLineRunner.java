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
        System.out.println("Type or paste a command ...");
        do {
            String commandPhrase = input.nextLine().toUpperCase().trim();
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

                case QUIT -> {
                    hasNotQuit.set(false);
                    System.exit(0);
                }
                default -> {
                    System.err.println("No such command see details below for help");
                    showHelpDetails();
                }

            }

        } while (hasNotQuit.get());

    }

    private static void printTitle() {
        printDashes();
        System.out.printf(HEADER_FORMAT, "|", "TODOLIST COMMANDLINE ASSIGNMENT", "|");
        printDashes();
    }

    private static void showHelpDetails() {
        printDashes();
        System.out.printf(HEADER_FORMAT, "|", "TODOLIST HELP SECTION", "|");
        printDashes();
        Arrays.stream(Command.values()).forEach(
                cmd -> System.out.printf(
                        "%-5s%-75s%-5s%n",
                        "|",
                        "Type or paste \"" + cmd.instruction() + "\" to " + cmd.tip(),
                        "|"));
        printDashes();
    }

    private static void listNamesOfTodos(TodoRepository repository) {
        AtomicInteger index = new AtomicInteger(1);
        printDashes();
        repository.listNamesOfTodos().forEach(todoName -> System.out.printf("\t%d.\s%s\n", index.getAndIncrement(), todoName));
        printDashes();
    }

    private static void viewTodoListByTodoName(TodoRepository repository, String todoName, Scanner input) {
        printDashes();
        try {
            Todo todoToBeViewed = repository.findTodoByName(todoName);
            System.out.println("Todo Name:\u0020" + todoToBeViewed.getName());
            printDashes();
            System.out.printf("%-40S%-20S%-10S", DESCRIPTION, DEADLINE, STATUS);
            printDashes();
            todoToBeViewed.getTasks().forEach(System.out::print);
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                System.out.println("What's the Todo name again?");
                String newTodoName;
                newTodoName = input.nextLine();
                viewTodoListByTodoName(repository, newTodoName, input);
            });
        }
        printDashes();
    }

    private static void appendTaskToTodo(TodoRepository repository, String todoName, Scanner input) {
        printDashes();
        try {
            System.out.println("What is the task description?");
            String description = input.nextLine();
            System.out.println("What is the task deadline in minutes");
            Long deadlineInMinutes = Long.parseLong(input.nextLine());
            Task taskToAppend = new Task(description, deadlineInMinutes);
            repository.appendTaskToEndOfTodo(todoName, taskToAppend);
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                String newTodoName;
                System.out.println("What's the Todo name again?");
                newTodoName = input.nextLine();
                appendTaskToTodo(repository, newTodoName, input);
            });
        }
        printDashes();
    }

    private static void removeTaskFromTodo(TodoRepository repository, String todoName, Scanner input) {
        printDashes();
        try {
            System.out.println("What's the position of this task?");
            int taskPosition = Integer.parseInt(input.nextLine());
            repository.remove(todoName, taskPosition);
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                String newTodoName;
                System.out.println("What's the todo name again?");
                newTodoName = input.nextLine();
                removeTaskFromTodo(repository, newTodoName, input);
            });
        }
    }

    private static void createNewTodo(TodoRepository repository, Todo todo, Scanner input) {
        printDashes();
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
        addAnotherTaskThenSaveNewTodo(repository, todo, input);
    }

    private static void checkTaskStatus(TodoRepository repository, String todoName, Scanner input) {
        printDashes();
        try {
            System.out.println("What's the position of this task?");
            int taskPosition = Integer.parseInt(input.nextLine());
            String status = repository.checkTaskStatus(todoName, taskPosition);
            System.out.printf("\nTask\sis\s%s\n", status);
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                String newTodoName;
                System.out.println("What's the todo name again?");
                newTodoName = input.nextLine();
                checkTaskStatus(repository, newTodoName, input);
            });
        }
    }

    private static void addAnotherTaskThenSaveNewTodo(TodoRepository repository, Todo todo, Scanner input) {
        try {
            switch (input.nextLine()) {
                case Y -> createNewTodo(repository, todo, input);
                case N -> repository.saveTodo(todo);
                default -> {
                    System.err.println("You are required to type either \"Y\" or\" +\n\"N\". Do you wish to continue?[Y/N]");
                    addAnotherTaskThenSaveNewTodo(repository, todo, input);
                }
            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage() + "\u0020Provide another todo Name");
            String newTodoName;
            System.out.println("What's the todo name again?");
            newTodoName = input.nextLine();
            todo.setName(newTodoName);
            repository.saveTodo(todo);
        }
    }

    private static void handleException(Scanner input, Runnable actionToPerform) {
        String reply = input.nextLine();
        switch (reply) {
            case Y -> {
                if (actionToPerform != null) {
                    actionToPerform.run();
                }
            }
            case N -> System.exit(0);
            default -> {
                System.err.println("\"You are required to type either \"Y\" or \"N\". Do you wish to continue?[Y/N]\"");
                handleException(input, actionToPerform);
            }
        }
    }

    private static void printDashes() {
        System.out.print("\n---------------------------------------------------------------------------------\n");
    }


}

