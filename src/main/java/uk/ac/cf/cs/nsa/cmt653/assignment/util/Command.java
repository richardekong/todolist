package uk.ac.cf.cs.nsa.cmt653.assignment.util;


import static uk.ac.cf.cs.nsa.cmt653.assignment.util.Constant.*;

public enum Command {

    LIST_ALL_TODO_NAMES(Constant.LIST_ALL_TODO_NAMES, LIST_ALL_TODO_NAMES_TIP),

    VIEW_TODO_LIST(Constant.VIEW_TODO_LIST, VIEW_TODO_LIST_TIP),

    APPEND_TASK(Constant.APPEND_TASK, APPEND_TASK_TIP),

    REMOVE_TASK(Constant.REMOVE_TASK, REMOVE_TASK_TIP),

    CREATE_NEW_TODO(Constant.CREATE_NEW_TODO, CREATE_NEW_TODO_TIP),

    RECORD_TASK_COMPLETION(Constant.RECORD_TASK_COMPLETION, RECORD_TASK_COMPLETION_TIP),

    QUIT(Constant.QUIT, QUIT_TIP),

    HELP(Constant.HELP, HELP_TIP);
    private final String instruction;
    private final String tip;

    Command(String instruction, String tip) {
        this.instruction = instruction;
        this.tip = tip;
    }

    public String instruction() {
        return instruction;
    }

    public String tip(){
        return tip;
    }

}

