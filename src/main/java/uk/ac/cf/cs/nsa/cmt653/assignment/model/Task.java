package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    private int id;
    private String description;
    private final LocalTime startTime;
    private Duration deadlineInMinutes;
    private Status status;
    private static final Long defaultDeadLineInMinutes = 10L;

    public Task(){
        id = 0;
        description = "Task-"+id;
        deadlineInMinutes = Duration.ofMinutes(defaultDeadLineInMinutes);
        startTime = LocalTime.now();
        status = Status.unDone;
    }
    public Task(String description, Long deadlineInMinutes) {
        this.id = 0;
        this.description = description;
        this.deadlineInMinutes = (deadlineInMinutes > 0L)
                ? Duration.ofMinutes(deadlineInMinutes)
                : Duration.ofMinutes(defaultDeadLineInMinutes);
        this.startTime = LocalTime.now();
        this.status = Status.unDone;
    }

    public void setID(int id){
        if (id <= 0 ){
            System.err.println("Invalid ID. ID must be > 0!");
            return;
        }
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDeadlineInMinutes(Long deadlineInMinutes) {
        if (deadlineInMinutes <= 0L) {
            System.err.println("Invalid Deadline!");
            return;
        }
        this.deadlineInMinutes = Duration.ofMinutes(deadlineInMinutes);
    }

    public Duration getDeadlineInMinutes() {
        return deadlineInMinutes;
    }

    public String getStatusString() {
        LocalTime estimatedDeadline = this.startTime.plus(deadlineInMinutes);
        LocalTime now = LocalTime.now();
        if (now.isAfter(estimatedDeadline)) {
            status = Status.done;
        }
        return status.status();
    }

}

