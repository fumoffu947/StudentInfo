package main.Creation.CourseCreation;

import main.DataStore.CourseGoalModel;
import main.DataStore.CourseGradeModel;
import main.DataStore.CourseInfo;
import main.DataStore.ShowPages.CoursesPage;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.PaneInterfaceSwitches.SwitchToCreateFromCourse;
import main.Interfaces.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by phili on 2016-04-10.
 */
public class CreateFromCourse implements Panel {

    private JPanel pageHolder = new JPanel();

    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    private JButton chooseCourseButton = new JButton();

    public CreateFromCourse(CoursesPage coursesPage, SwitchToAddStudentTeacherToCourse switchToAddStudentTeacherToCourse) {
        pageHolder.setLayout(new GridBagLayout());

        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridy = 0;
        nameConstraints.weightx = 1.0;
        nameConstraints.weighty = 0.5;
        nameConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints coursesConstraints = new GridBagConstraints();
        coursesConstraints.gridy = 1;
        coursesConstraints.weightx = 1.0;
        coursesConstraints.weighty = 40.0;
        coursesConstraints.fill = GridBagConstraints.BOTH;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        JTextField tipNameField = new JTextField("New Course Name");
        tipNameField.setEditable(false);
        JTextField courseNameField = new JTextField();
        container.add(tipNameField);
        container.add(courseNameField);
        pageHolder.add(container, nameConstraints);

        tableModel.addColumn("Courses");
        for (CourseInfo courseInfo: coursesPage.getCourses()) {
            tableModel.addRow(new Object[] {courseInfo.getCourseName()});
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chooseCourseButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = courseNameField.getText();
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1 && newName != null && !newName.isEmpty() && !coursesPage.containsCourseWithName(newName)) {
                    CourseInfo courseInfo = coursesPage.getCourses().get(selectedRow);
                    CourseGradeModel courseGradeClone = courseInfo.getCourseGradeModel().getClone();
                    CourseGoalModel courseGoalClone = courseInfo.getCourseGoalModel().getClone();
                    switchToAddStudentTeacherToCourse.startChooseGroupPage(newName,
                            courseGoalClone, courseGradeClone);
                }
            }
        });
        chooseCourseButton.setText("Choose Course");
        pageHolder.add(new JScrollPane(table), coursesConstraints);
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.add(chooseCourseButton);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        jMenuBar.add(chooseCourseButton);
    }

    @Override
    public JPanel getPageHolder() {
        return pageHolder;
    }
}
