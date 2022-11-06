package uk.ac.cf.cs.nsa.cmt653.assignment.util;


public enum Command {

    LIST_ALL_TODO_NAMES(Constant.LIST_ALL_TODO_NAMES, () -> {
    }),
    VIEW_TODO_LIST(Constant.VIEW_TODO_LIST, () -> {
    }),
    APPEND_TASK(Constant.APPEND_TASK, () -> {
    }),
    REMOVE_TASK(Constant.REMOVE_TASK, () -> {
    }),
    CREATE_NEW_TODO(Constant.CREATE_NEW_TODO, () -> {
    }),
    RECORD_TASK_COMPLETION(Constant.RECORD_TASK_COMPLETION, () -> {
    }),
    QUIT(Constant.QUIT, () -> System.exit(0)),

    HELP(Constant.HELP, () -> {

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

