package uk.ac.cf.cs.nsa.cmt653.assignment.utils;

public interface IDGenerator {

    default <T extends Class<T>> int generate(T whichClass){
        return 0;
    }
}
