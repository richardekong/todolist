package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {

    private int id;
    private String description;
    private final LocalTime startTime;
    private long deadlineInMinutes;
    private String status;
    private static final Long defaultDeadLineInMinutes = 1L;

    public Task() {
        description = "New-" + getClass().getSimpleName();
        startTime = LocalTime.now();
        deadlineInMinutes = defaultDeadLineInMinutes;
        status = Status.scheduled.status();
    }

    public Task(String description, Long deadlineInMinutes) {
        this.description = description;
        this.startTime = LocalTime.now();
        this.deadlineInMinutes = (deadlineInMinutes > 0L) ? deadlineInMinutes : defaultDeadLineInMinutes;
        this.status = Status.scheduled.status();
    }

    public void setId(int id) {
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
        this.deadlineInMinutes = (deadlineInMinutes > 0L)
                ? deadlineInMinutes
                : defaultDeadLineInMinutes;
    }

    public long getDeadlineInMinutes() {
        return deadlineInMinutes;
    }

    public void setStatus(Status status) {
        if (this.status.equals(Status.closed.status())) {
            System.err.printf("Task with id number %d is closed\n", id);
            return;
        } else if (this.status.equals(Status.done.status()) && getTimeLeftInMinutes() <= 0) {
            System.err.printf("Status of Task with id number %d is done %s\n", id, status.status());
            return;
        } else {
            this.status = status.status();
            System.out.printf("Task with id number %d is now set to %s\n", id, status.status());
        }
    }

    public String getStatus() {
        String currentStatus = status;
        LocalTime estimatedDeadline = this.startTime.plus(Duration.ofMinutes(deadlineInMinutes));
        LocalTime now = LocalTime.now();
        if (now.isAfter(estimatedDeadline) && !status.equals(Status.done.status())) {
            status = Status.closed.status();
        } else {
            status = currentStatus;
        }
        return status;
    }

    public long getTimeSpentInMinutes() {
        long timeSpentInMinutes = (LocalTime.now().getMinute() - startTime.getMinute());
        return Math.min(timeSpentInMinutes, deadlineInMinutes);
    }

    public long getTimeLeftInMinutes() {
        return deadlineInMinutes - getTimeSpentInMinutes();
    }

    public boolean isClosed() {
        return status.equals(Status.closed.status());
    }

    @Override
    public String toString() {
        final String minutes = "Minutes";
        return String.format(
                "%-10d%-40s%-20s%-20s%-20s%-10s%n",
                getId(),
                getDescription(),
                getDeadlineInMinutes() + "\s" + minutes,
                (getTimeSpentInMinutes() > 0 ? getTimeSpentInMinutes() + "\s" + minutes : "-"),
                getTimeLeftInMinutes() > 0 ? getTimeLeftInMinutes() + "\s" + minutes : "-",
                getStatus());
    }
}

