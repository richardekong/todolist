package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Task;
import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum TodoDataStore {

    INSTANCE;
    private final Task todo1Task1 = new Task("Buy food stuff from the Store", 30L);
    private final Task todo1Task2 = new Task("Arrange food stuff in kitchen", 10L);
    private final Task todo1Task3 = new Task("Clean the Kitchen", 10L);
    private final Task todo1Task4 = new Task("Pour water into the pot", 3L);
    private final Task todo1Task5 = new Task("Boil water in the pot", 10L);
    private final Task todo1Task6 = new Task("Pour the food in the boiling water", 2L);
    private final Task todo1Task7 = new Task("Finish cooking", 15L);
    private final Todo todo1 = new Todo("Cooking", new LinkedList<>(List.of(
            todo1Task1, todo1Task2, todo1Task3, todo1Task4, todo1Task5, todo1Task6, todo1Task7
    )));

    private final Task todo2Task1 = new Task("Attend Agile Development", 15L);
    private final Task todo2Task2 = new Task("Attend Programming Principles", 15L);
    private final Task todo2Task3 = new Task("Attend Web Applications", 15L);

    private final Todo todo2 = new Todo("Lectures", new LinkedList<>(
            List.of(todo2Task1, todo2Task2, todo2Task3)
    ));

    private final LinkedHashMap<String, Todo> todoDataStore = new LinkedHashMap<>(
            Map.of(
                    todo1.getName(), todo1,
                    todo2.getName(), todo2
            )
    );

    public LinkedHashMap<String, Todo> getDataStore() {
        return todoDataStore;
    }
}

