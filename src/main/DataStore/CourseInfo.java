package main.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by fumoffu on 2015-10-20.
 * namn
 * (det som ska betyg sättas)
 * ( krav i lista varje krav kan ha fler del krav)
 * (första sida som är summering av separate elev blad
 *
 *
 */
public class CourseInfo {

    private ClassInfo classInfo;
    private List<Student> otherEnlistedStudents;
    private String courseName;
    private List<Teacher> teachers;
    private CourseGoalModel courseGoalModel;

    public CourseInfo(final ClassInfo classInfo, final List<Student> otherEnlistedStudents, final String courseName,
                      final List<Teacher> teachers, CourseGoalModel courseGoalModel) {

        this.classInfo = classInfo;
        this.otherEnlistedStudents = otherEnlistedStudents;
        this.courseName = courseName;
        this.teachers = teachers;
        this.courseGoalModel = courseGoalModel;
    }

    public ClassInfo getClassInfo() {
	return classInfo;
    }

    public List<Student> getOtherEnlistedStudents() {
	return otherEnlistedStudents;
    }

    public String getCourseName() {
	return courseName;
    }

    public List<Teacher> getTeachers() {
	return teachers;
    }

    public void setClassInfo(final ClassInfo classInfo) {
	this.classInfo = classInfo;
    }

    public void setOtherEnlistedStudents(final List<Student> otherEnlistedStudents) {
	this.otherEnlistedStudents = otherEnlistedStudents;
    }

    public void setCourseName(final String courseName) {
	this.courseName = courseName;
    }

    public void setTeachers(final List<Teacher> teachers) {
	this.teachers = teachers;
    }

    public boolean addTeacher(final Teacher teacher) {
        return this.teachers.add(teacher);
    }

    public CourseGoalModel getCourseGoalModel() {
        return courseGoalModel;
    }
}
