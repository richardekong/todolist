package uk.ac.cf.cs.nsa.cmt653.assignment;

import uk.ac.cf.cs.nsa.cmt653.assignment.manager.TodoManager;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Status;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;
import uk.ac.cf.cs.nsa.cmt653.assignment.repository.TodoRepository;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static uk.ac.cf.cs.nsa.cmt653.assignment.util.Constant.*;


public class CommandLineRunner {

    public static void main(String[] args) {
        useCommands();
    }

    private static void useCommands() {
        TodoRepository repo = new TodoManager();
        Scanner input = new Scanner(System.in);
        AtomicBoolean hasNotQuit = new AtomicBoolean(true);
        printTitle();
        showHelpDetails();
        do {
            System.out.println("Type or paste a command ...");
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
                case CHECK_TASK_STATUS -> {
                    System.out.println("What's the Todo Name?");
                    String todoName = input.nextLine();
                    checkTaskStatus(repo, todoName, input);
                }
                case RECORD_TASK_COMPLETION -> recordTaskCompletion(repo, input);

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
                        "%-20s%-98s%-13s%n",
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
            System.out.printf("%-10S%-40S%-20S%-20S%-20S%-10S", ID, DESCRIPTION, DEADLINE, TIME_SPENT, TIME_LEFT, STATUS);
            printDashes();
            todoToBeViewed.getTasks().forEach(System.out::print);
        } catch (RuntimeException rte) {
            System.err.println(rte.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                System.out.println("What's the Todo name again?");
                String newTodoName = input.nextLine();
                viewTodoListByTodoName(repository, newTodoName, input);
            });
        }
        printDashes();
    }

    private static void appendTaskToTodo(TodoRepository repository, String todoName, Scanner input) {
        String description = "";
        long deadlineInMinutes;
        printDashes();
        try {
            System.out.println("What is the task description?");
            description = input.nextLine();
            System.out.println("What is the task deadline in minutes");
            deadlineInMinutes = Long.parseLong(input.nextLine());
            Task taskToAppend = new Task(description, deadlineInMinutes);
            repository.appendTaskToEndOfTodo(todoName, taskToAppend);
        } catch (RuntimeException ex) {
            if (ex instanceof NumberFormatException) {
                final String originalDescription = description;
                System.err.println("Wrong value! Do you wish to continue? [Y/N]");
                handleException(input, () -> {
                    System.out.println("What's the task's deadline in minutes again?");
                    Long correctDeadlineInMinutes = Long.parseLong(input.nextLine());
                    Task newTaskToAppend = new Task(originalDescription, correctDeadlineInMinutes);
                    repository.appendTaskToEndOfTodo(todoName, newTaskToAppend);
                });
                return;
            }
            System.err.println(ex.getMessage() + "\u0020Do you wish to continue?[Y/N]");
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
            System.out.println("What's the task ID?");
            int taskId = Integer.parseInt(input.nextLine());
            repository.remove(todoName, taskId);
        } catch (RuntimeException ex) {
            if (ex instanceof NumberFormatException) {
                System.err.println("Wrong value! Please do you wish to continue? [Y/N]");
                handleException(input, () -> {
                    System.out.println("What's the task ID again?");
                    repository.remove(todoName, Integer.parseInt(input.nextLine()));
                });
                return;
            }
            System.err.println(ex.getMessage() + "\u0020Please do you wish to continue?[Y/N]");
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
        AtomicLong deadlineInMinutes = new AtomicLong(0);
        try {
            deadlineInMinutes.set(Long.parseLong(input.nextLine()));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong value! Do you wish to continue? [Y/N]");
            handleException(input, () -> {
                System.out.println("What's the task deadline in minutes again? eg numbers like 1,2 etc");
                deadlineInMinutes.set(Long.parseLong(input.nextLine()));
            });
        }
        Task task = new Task(description, deadlineInMinutes.get());
        boolean taskAdded = todo.add(task);
        if (taskAdded) {
            System.out.println("Task added!");
        }
        System.out.println("Do you want to add another task?[Y/N]");
        addAnotherTaskThenSaveNewTodo(repository, todo, input);
    }

    private static void checkTaskStatus(TodoRepository repository, String todoName, Scanner input) {
        printDashes();
        try {
            System.out.println("What's the task's ID?");
            int taskPosition = Integer.parseInt(input.nextLine());
            String status = repository.checkTaskStatus(todoName, taskPosition);
            System.out.printf("\nTask\sis\s%s\n", status);
        } catch (RuntimeException ex) {
            if (ex instanceof NumberFormatException) {
                System.err.println("Wrong value, do you wish to continue?[Y/N]");
                handleException(input, () -> {
                    System.out.println("What's the task's ID again? eg numbers like 1,2 etc");
                    System.out.printf("\nTask\sis\s%s\n", repository.checkTaskStatus(todoName, Integer.parseInt(input.nextLine())));
                });
                return;
            }
            System.err.println(ex.getMessage() + "\u0020Do you wish to continue?[Y/N]");
            handleException(input, () -> {
                String newTodoName;
                System.out.println("What's the todo name again?");
                newTodoName = input.nextLine();
                checkTaskStatus(repository, newTodoName, input);
            });
        }
        printDashes();
    }
    private static void recordTaskCompletion(TodoRepository repository, Scanner input){
        printDashes();
        try{
            System.out.println("What's the Todo Name?");
            String todoName = input.nextLine();
            System.out.println("What's the task's ID?");
            int taskId = Integer.parseInt(input.nextLine());
            System.out.printf("Please have you completed task with Id number %d? [Y/N]\n", taskId);
            String reply = input.nextLine()
                    .toUpperCase()
                    .trim();
            switch (reply){
                case Y -> repository.recordTaskCompletion(todoName, taskId, Status.done);
                case N -> repository.recordTaskCompletion(todoName, taskId, Status.unDone);
                default -> {
                    System.err.println("You are required to type either \"Y\" or\" +\n\"N\". Please do you wish to repeat the record completion process?[Y/N]");
                    recordTaskCompletion(repository, input);
                }
            }
        }catch (RuntimeException ex){
            if (ex instanceof NumberFormatException){
                System.err.println("Wrong value, please do you wish to repeat the record completion process?[Y/N]");
                handleException(input, () ->recordTaskCompletion(repository, input));
                return;
            }
            System.err.println(ex.getMessage());
            System.out.println("Please do you wish to repeat the record completion process?[Y/N]");
            handleException(input, () -> recordTaskCompletion(repository, input));
        }

    }

    private static void addAnotherTaskThenSaveNewTodo(TodoRepository repository, Todo todo, Scanner input) {
        try {
            String reply = input.nextLine()
                    .toUpperCase()
                    .trim();
            switch (reply) {
                case Y -> createNewTodo(repository, todo, input);
                case N -> repository.saveTodo(todo);
                default -> {
                    System.err.println("You are required to type either \"Y\" or \"N\". Do you wish to continue?[Y/N]");
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
        String reply = input.nextLine().toUpperCase().trim();
        try {
            switch (reply) {
                case Y -> {
                    if (actionToPerform != null) {
                        actionToPerform.run();
                    }
                }
                case N -> useCommands();
                default -> {
                    System.err.println("\"You are required to type either \"Y\" or \"N\". Do you wish to continue?[Y/N]\"");
                    handleException(input, actionToPerform);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage() + "\u0020Do you still wish to continue?[Y?N]");
            handleException(input, actionToPerform);
        }

    }

    private static void printDashes() {
        final int dashesToPrint = 120;
        final String dash = "-";
        System.out.println();
        for (int i = 1; i < dashesToPrint; i++) {
            System.out.print(dash);
        }
        System.out.println();
    }

}

