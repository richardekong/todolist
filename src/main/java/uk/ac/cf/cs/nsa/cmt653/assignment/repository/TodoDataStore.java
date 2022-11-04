package uk.ac.cf.cs.nsa.cmt653.assignment.repository;

import uk.ac.cf.cs.nsa.cmt653.assignment.model.Todo;

import java.util.LinkedHashMap;
import java.util.Map;

public class TodoDataStore {

    public static final LinkedHashMap<String, Todo> todoDataStore = (LinkedHashMap<String, Todo>) Map.of(
            "Todo1", new Todo(), "Todo2", new Todo()
    );
}
