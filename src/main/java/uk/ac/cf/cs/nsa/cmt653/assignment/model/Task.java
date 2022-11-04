package uk.ac.cf.cs.nsa.cmt653.assignment.model;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    private final int id;
    private String description;
    private final LocalTime startTime;
    private Duration deadlineInMinutes;
    private Status status;

    public Task(int id, String description, Long deadlineInMinutes) {
        this.id = id;
        this.description = description;
        this.deadlineInMinutes = Duration.ofMinutes(deadlineInMinutes);
        this.startTime = LocalTime.now();
        this.status = Status.unDone;
    }

    public int getId(){
        return id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setDeadlineInMinutes(Long deadlineInMinutes){
        this.deadlineInMinutes = Duration.ofMinutes(deadlineInMinutes);
    }

    public Duration getDeadlineInMinutes(){
        return deadlineInMinutes;
    }

    public String getStatusString(){
        LocalTime estimatedDeadline = this.startTime.plus(deadlineInMinutes);
        LocalTime now = LocalTime.now();
        if (now.isAfter(estimatedDeadline)){
            status = Status.done;
        }
        return status.status();
    }

    public enum Status {
        unDone("undone"), done("done");
        private final String status;
        Status(String status) {
            this.status = status;
        }
        public String status() {
            return status;
        }

    }

}

