package main.Interfaces.InterfaceDataTransfer;

import main.DataStore.CourseInfo;

/**
 * Created by phili on 2016-03-29.
 */
public interface CourseInfoTransfer {
    CourseInfo getCourseInfoByName(String name);
}
