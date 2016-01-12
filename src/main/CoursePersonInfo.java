package main;

import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 * namn
 * (det som ska betyg s�ttas)
 * ( krav i lista varje kravn kan ha fler del krav)
 * (f�rsta sida som �r sumering av separate elev blad
 *
 *
 */
public class CoursePersonInfo
{

    private ClassInfo classInfo;
    private List<Student> otherEnlistedStudents;
    private String courseName;
    private Teacher teacher;

    public CoursePersonInfo(final ClassInfo classInfo, final List<Student> otherEnlistedStudents, final String courseName,
			    final Teacher teacher)
    {
	this.classInfo = classInfo;
	this.otherEnlistedStudents = otherEnlistedStudents;
	this.courseName = courseName;
	this.teacher = teacher;
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

    public Teacher getTeacher() {
	return teacher;
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

    public void setTeacher(final Teacher teacher) {
	this.teacher = teacher;
    }
}
