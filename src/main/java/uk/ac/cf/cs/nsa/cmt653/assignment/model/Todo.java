package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Todo {
    private String name;
    private LinkedList<Task> tasks;
    private final LocalTime createAt;

    private static final LinkedList<Task> defaultTasks = new LinkedList<>(List.of(new Task(), new Task()));

    public Todo() {
        name = "New-" + this.getClass().getSimpleName();
        tasks = defaultTasks;
        createAt = LocalTime.now();
    }

    public Todo(String name, LinkedList<Task> tasks){
        this.name = name;
        this.tasks = tasks;
        createAt = LocalTime.now();
    }
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public  void setTasks(Task ... tasks){
        this.tasks = new LinkedList<>(List.of(tasks));
    }

    public LinkedList<Task> getTasks(){
        return tasks;
    }

}

