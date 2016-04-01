package main.DataStore;

import java.util.ArrayList;
import java.util.List;

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

    private String courseName;

    private List<ClassInfo> classInfoList;
    private List<Student> otherEnlistedStudents;
    private List<Teacher> teachers;

    private CourseGoalModel courseGoalModel;
    private CourseGradeModel courseGradeModel;

    private List<Integer> listOfRemovedGroupStudents;

    public CourseInfo(final List<ClassInfo> classInfoList, final List<Student> otherEnlistedStudents, final String courseName,
                      final List<Teacher> teachers, CourseGoalModel courseGoalModel, CourseGradeModel courseGradeModel) {

        this.classInfoList = classInfoList;
        this.otherEnlistedStudents = otherEnlistedStudents;
        this.courseName = courseName;
        this.teachers = teachers;
        this.courseGoalModel = courseGoalModel;
        this.listOfRemovedGroupStudents = new ArrayList<>();
        this.courseGradeModel = courseGradeModel;
    }

    public CourseInfo(final List<ClassInfo> classInfoList, final List<Student> otherEnlistedStudents, final String courseName,
                      final List<Teacher> teachers, CourseGoalModel courseGoalModel, List<Integer> listOfRemovedGroupStudents,
                      CourseGradeModel courseGradeModel) {

        this.classInfoList = classInfoList;
        this.otherEnlistedStudents = otherEnlistedStudents;
        this.courseName = courseName;
        this.teachers = teachers;
        this.courseGoalModel = courseGoalModel;
        this.listOfRemovedGroupStudents = listOfRemovedGroupStudents;
        this.courseGradeModel = courseGradeModel;
    }

    public List<ClassInfo> getClassInfoList() {
	return classInfoList;
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

    public void setClassInfoList(final List<ClassInfo> classInfoList) {
	this.classInfoList = classInfoList;
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

    public boolean removeStudent(int studentID) {
        return listOfRemovedGroupStudents.add(studentID);
    }

    public boolean removeOtherEnlistedStudent(Student student) {
        return otherEnlistedStudents.remove(student);
    }

    public CourseGoalModel getCourseGoalModel() {
        return courseGoalModel;
    }

    public List<Integer> getListOfRemovedGroupStudents() {
        return listOfRemovedGroupStudents;
    }

    public CourseGradeModel getCourseGradeModel() {
        return courseGradeModel;
    }
}
