package main.DataStore;

import main.Comparators.StudentComparator;

import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class ClassInfo  {
    private List<Student> students;
    private String className;

    public ClassInfo(final List<Student> students, final String className) {
	this.students = students;
	this.className = className;
        students.sort(new StudentComparator());
    }

    public List<Student> getStudents() {
	return students;
    }

    public String getClassName() {
	return className;
    }

    public void setStudents(final List<Student> students) {
	this.students = students;
	this.students.sort(new StudentComparator());
    }

    public void addStudent(Student student) {
        students.add(student);
        students.sort(new StudentComparator());
    }

    public void setClassName(final String className) {
	this.className = className;
    }
}
