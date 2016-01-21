package main.DataStore;

import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Created by phili on 2016-01-16.
 * this is the page where all the courses can be found
 */
public class CoursesPage {

    private List<CourseInfo> courses;

    private JPanel pageHolder = new JPanel(new GridBagLayout());

    private DefaultTableModel coursesAndInfoTableModel = new DefaultTableModel();

    private JTable coursesInfoTable = new JTable(coursesAndInfoTableModel);

    public CoursesPage(List<CourseInfo> courses, SwitchToStudentCourseGrade switchToStudentCourseGrade) {
        this.courses = courses;

        // add the columns
        coursesAndInfoTableModel.addColumn("Course Name");
        coursesAndInfoTableModel.addColumn("Number Of Teachers");
        coursesAndInfoTableModel.addColumn("Class name");
        coursesAndInfoTableModel.addColumn("Number Of Students From Class");
        coursesAndInfoTableModel.addColumn("Other Enlisted Students");



        //add all the info about each course to a row in the right orded
        for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++) {
            CourseInfo courseInfo = courses.get(courseIndex);
            coursesAndInfoTableModel.addRow(new Object[] {courseInfo.getCourseName(),courseInfo.getTeachers().size(),
                                                            courseInfo.getClassInfo().getClassName(), courseInfo.getClassInfo().getStudents().size(),
                                                            courseInfo.getOtherEnlistedStudents().size()});
        }

        // set so that the user only can select one course and so that that course will be show with the info
        coursesInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesInfoTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                switchToStudentCourseGrade.switchToCourseGradePage(courses.get(coursesInfoTable.getSelectedRow()));
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });

        // add an header to the page
        JTextField pageName = new JTextField("Courses");
        pageName.setEditable(false);
        GridBagConstraints pageNameConstraints = new GridBagConstraints();
        pageNameConstraints.gridx = 0;
        pageNameConstraints.gridy = 0;
        pageNameConstraints.weightx = 1.0;
        pageNameConstraints.fill = GridBagConstraints.HORIZONTAL;
        pageHolder.add(pageName,pageNameConstraints);

        // add the table with the course info to the pageHolder
        GridBagConstraints courseInfoTableConstraints = new GridBagConstraints();
        courseInfoTableConstraints.gridx = 0;
        courseInfoTableConstraints.gridy = 1;
        courseInfoTableConstraints.weightx = 1.0;
        courseInfoTableConstraints.weighty = 2.0;
        courseInfoTableConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(new JScrollPane(coursesInfoTable), courseInfoTableConstraints);


    }

    public void addCourse(CourseInfo courseInfo) {
        coursesAndInfoTableModel.addRow(new Object[] {courseInfo.getCourseName(),courseInfo.getTeachers().size(),
                courseInfo.getClassInfo().getClassName(), courseInfo.getClassInfo().getStudents().size(),
                courseInfo.getOtherEnlistedStudents().size()});
        courses.add(courseInfo);
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }
}
