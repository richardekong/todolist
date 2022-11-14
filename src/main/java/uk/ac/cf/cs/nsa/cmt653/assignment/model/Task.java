package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    private String description;
    private final LocalTime startTime;
    private Duration deadlineInMinutes;
    private Duration timeSpentInMinutes;
    private Status status;
    private static final Long defaultDeadLineInMinutes = 10L;
    public Task() {
        description = "New-" + getClass().getSimpleName();
        startTime = LocalTime.now();
        deadlineInMinutes = Duration.ofMinutes(defaultDeadLineInMinutes);
        timeSpentInMinutes = Duration.ofMinutes(getTimeSpentInMinutes());
        status = Status.unDone;
    }

    public Task(String description, Long deadlineInMinutes) {
        this.description = description;
        this.startTime = LocalTime.now();
        this.deadlineInMinutes = (deadlineInMinutes > 0L)
                ? Duration.ofMinutes(deadlineInMinutes)
                : Duration.ofMinutes(defaultDeadLineInMinutes);
        timeSpentInMinutes = Duration.ofMinutes(getTimeSpentInMinutes());
        this.status = Status.unDone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDeadlineInMinutes(Long deadlineInMinutes) {
        this.deadlineInMinutes = (deadlineInMinutes > 0L)
                ? Duration.ofMinutes(deadlineInMinutes)
                : Duration.ofMinutes(defaultDeadLineInMinutes);
    }

    public long getDeadlineInMinutes() {
        return deadlineInMinutes.getSeconds() / 60;
    }

    public String getStatusString() {
        LocalTime estimatedDeadline = this.startTime.plus(deadlineInMinutes);
        LocalTime now = LocalTime.now();
        if (now.isAfter(estimatedDeadline)) {
            status = Status.done;
        }
        return status.status();
    }
    private Long getTimeSpentInMinutes() {
        long timeDifference =  Integer.toUnsignedLong(LocalTime.now().getMinute() - startTime.getMinute());
        long deadline = getDeadlineInMinutes();
        if (timeDifference > deadline) {
            timeSpentInMinutes = Duration.ofMinutes(deadline);
            return deadline;
        }
        timeSpentInMinutes = Duration.ofMinutes(timeDifference);
        return timeDifference;
    }

    @Override
    public String toString() {
        return String.format(
                "%-40s%-20s%-20s%-10s%n",
                description,
                getDeadlineInMinutes() + "\sMinutes",
                getTimeSpentInMinutes() + "\sMinutes",
                getStatusString());
    }
}

