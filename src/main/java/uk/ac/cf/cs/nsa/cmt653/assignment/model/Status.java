package uk.ac.cf.cs.nsa.cmt653.assignment.model;

public enum Status {
    scheduled("scheduled"),
    unDone("undone"),
    done("done"),
    closed("closed");
    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}

