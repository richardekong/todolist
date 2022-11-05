package uk.ac.cf.cs.nsa.cmt653.assignment.util;


import static uk.ac.cf.cs.nsa.cmt653.assignment.util.Constant.*;

public enum Command {
    //initialize enum items with Runnable still not sure if this is good,
    // please I need your expert advice
    listAllTodoNames(LIST_ALL_TODO_NAMES, () -> {
    }),
    viewATodoList(VIEW_TODO_LIST, () -> {
    }),
    AppendTask(APPEND_TASK, () -> {
    }),
    RemoveTask(REMOVE_TASK, () -> {
    }),
    CreateNewTodo(CREATE_NEW_TODO, () -> {
    }),
    recordTaskCompletion(RECORD_TASK_COMPLETION, () -> {
    });
    private Runnable executionBlock;
    private final String instruction;

    Command(String instruction, Runnable runnable) {
        this.instruction = instruction;
        this.executionBlock = runnable;
    }

    public void setExecutionBlock(Runnable executionBlock) throws NullPointerException {
        if (executionBlock == null) {
            throw new NullPointerException();
        }
        this.executionBlock = executionBlock;
        this.executionBlock.run();
    }

    public String instruction() {
        return instruction;
    }

}

