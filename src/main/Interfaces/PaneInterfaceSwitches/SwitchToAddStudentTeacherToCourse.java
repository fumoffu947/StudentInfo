package main.Interfaces.PaneInterfaceSwitches;

import main.DataStore.ClassInfo;
import main.DataStore.CourseGoalModel;

import java.util.List;

/**
 * Created by philip on 2016-01-15.
 */
public interface SwitchToAddStudentTeacherToCourse {

    void startAddStudentTeacherToCourse(String courseName, CourseGoalModel  courseGoalModel, List<ClassInfo> classInfo);

    void startChooseGroupPage(String courseName, CourseGoalModel  courseGoalModel);
}
