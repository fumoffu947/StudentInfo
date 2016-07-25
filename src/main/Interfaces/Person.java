package main.Interfaces;

/**
 * Created by philip on 2016-01-13.
 * This interface is supposed to be used by classes that are different persons
 */
public interface Person {

    boolean isTeacher();

    String getFirstName();

    @Override
    String toString();

    int getID();

    // int getDBID(); istead of getID();
}
