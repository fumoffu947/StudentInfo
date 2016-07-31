package main.Interfaces.PaneInterfaceSwitches;

import main.DataStore.CoursePakage.CourseInfo;

/**
 * Created by phili on 2016-01-19.
 */
public interface SwitchToStudentCourseGrade {
    void switchToCourseGradePage(CourseInfo courseInfo);

    void switchToCourseGradePageAndAddCourseInfo(CourseInfo courseInfo);
}
