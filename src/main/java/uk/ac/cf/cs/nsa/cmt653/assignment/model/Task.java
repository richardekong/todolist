package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    private String description;
    private final LocalTime startTime;
    private long deadlineInMinutes;
    private String status;
    private static final Long defaultDeadLineInMinutes = 1L;

    public Task() {
        description = "New-" + getClass().getSimpleName();
        startTime = LocalTime.now();
        deadlineInMinutes = defaultDeadLineInMinutes;
        status = Status.unDone.status();
    }

    public Task(String description, Long deadlineInMinutes) {
        this.description = description;
        this.startTime = LocalTime.now();
        this.deadlineInMinutes = (deadlineInMinutes > 0L) ? deadlineInMinutes : defaultDeadLineInMinutes;
        this.status = Status.unDone.status();
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

    public String getStatus() {
        LocalTime estimatedDeadline = this.startTime.plus(Duration.ofMinutes(deadlineInMinutes));
        LocalTime now = LocalTime.now();
        if (now.isAfter(estimatedDeadline)) {
            status = Status.done.status();
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

    @Override
    public String toString() {
        final String minutes = "Minutes";
        return String.format(
                "%-40s%-20s%-20s%-20s%-10s%n",
                getDescription(),
                getDeadlineInMinutes() + "\s" + minutes,
                (getTimeSpentInMinutes() > 0 ? getTimeSpentInMinutes() + "\s" + minutes : "-"),
                getTimeLeftInMinutes() > 0 ? getTimeLeftInMinutes() + "\s" + minutes : "-",
                getStatus());
    }
}

