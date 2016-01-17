package main.DataStore;

import main.Interfaces.Person;

import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class Teacher extends Student implements Person {

    public Teacher(List<String> name, String lastName, int firstName, String cellPhoneNumber, String emailAddress, int ID) {
        super(name, lastName, firstName, cellPhoneNumber, emailAddress, ID);
    }

    @Override
    public boolean isTeacher() {
        return true;
    }

}
