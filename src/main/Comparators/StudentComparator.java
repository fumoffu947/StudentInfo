package main.Comparators;

import main.DataStore.Student;

import java.util.Comparator;

/**
 * Created by fumoffu on 2015-10-24.
 */
public class StudentComparator implements Comparator<Student>
{
    @Override public int compare(final Student o1, final Student o2) {
	return o1.getSurname().compareTo(o2.getSurname());
    }
}
