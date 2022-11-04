package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    private final int id;
    private String description;
    private final LocalTime startTime;
    private Duration deadlineInMinutes;
    private Status status;
    private static final Long defaultDeadLineInMinutes = 10L;

    public Task(){
        id = IDGenerator.generate(this.getClass());
        description = "Task-"+id;
        deadlineInMinutes = Duration.ofMinutes(defaultDeadLineInMinutes);
        startTime = LocalTime.now();
        status = Status.unDone;
    }
    public Task(String description, Long deadlineInMinutes) {
        this.id = IdGenerator.generate(this.getClass());
        this.description = description;
        this.deadlineInMinutes = (deadlineInMinutes > 0L)
                ? Duration.ofMinutes(deadlineInMinutes)
                : Duration.ofMinutes(defaultDeadLineInMinutes);
        this.startTime = LocalTime.now();
        this.status = Status.unDone;
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

