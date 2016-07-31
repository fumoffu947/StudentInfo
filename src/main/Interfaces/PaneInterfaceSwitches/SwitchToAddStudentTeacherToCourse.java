package main.Interfaces.PaneInterfaceSwitches;

import main.DataStore.ClassInfo;
import main.DataStore.CoursePakage.CourseGoalModel;
import main.DataStore.CoursePakage.CourseGradeModel;

import java.util.List;

/**
 * Created by philip on 2016-01-15.
 */
public interface SwitchToAddStudentTeacherToCourse {

    void startAddStudentTeacherToCourse(String courseName, CourseGoalModel  courseGoalModel, CourseGradeModel courseGradeModel, List<ClassInfo> classInfo);

    void startChooseGroupPage(String courseName, CourseGoalModel  courseGoalModel, CourseGradeModel courseGradeModel);
}
