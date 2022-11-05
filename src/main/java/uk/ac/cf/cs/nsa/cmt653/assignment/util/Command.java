package uk.ac.cf.cs.nsa.cmt653.assignment.util;

public enum Command {
    //initialize enum items with Runnable still not sure if this is good,
    // please I need your expert advice
    listAllTodoNames(() -> {
    }),
    viewATodo(() -> {
    }),
    AppendTask(() -> {
    }),
    RemoveTask(() -> {
    }),
    CreateNewTodo(() -> {
    }),
    recordTaskCompletion(() -> {
    });
    private Runnable executionBlock;

    Command(Runnable runnable) {
        this.executionBlock = runnable;
    }

    public void setExecutionBlock(Runnable executionBlock) throws NullPointerException {
        if (executionBlock == null) {
            throw new NullPointerException();
        }
        this.executionBlock = executionBlock;
        this.executionBlock.run();
    }
}



