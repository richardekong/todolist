package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Todo {
    private String name;
    private final LinkedList<Task> tasks = new LinkedList<>();
    private final LocalTime createAt;
    private static final LinkedList<Task> defaultTasks = new LinkedList<>(List.of(new Task(), new Task()));

    public Todo() {
        name = "New-" + this.getClass().getSimpleName();
        createAt = LocalTime.now();
        add(defaultTasks);
    }

    public Todo(String name, LinkedList<Task> tasks) {
        this.name = name;
        createAt = LocalTime.now();
        add(tasks);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public boolean add(LinkedList<Task> tasks) {
        int nextId = getNextId();
        AtomicInteger atomicId = new AtomicInteger(nextId);
        tasks.forEach(task -> task.setId(atomicId.getAndIncrement()));
        return this.tasks.addAll(tasks);
    }

    public boolean add(Task task) {
        int nextId = getNextId();
        task.setId(nextId);
        return tasks.add(task);
    }

    private int getNextId() {
        return tasks.size() + 1;
    }

}

