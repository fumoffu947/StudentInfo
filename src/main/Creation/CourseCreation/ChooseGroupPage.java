package main.Creation.CourseCreation;

import main.DataStore.ClassInfo;
import main.DataStore.CourseGoalModel;
import main.DataStore.MyJTable;
import main.DataStore.Student;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2016-02-17.
 */
public class ChooseGroupPage implements Panel {

    private JPanel pageHolder = new JPanel(new GridBagLayout());
    private JButton continueButton;


    private DefaultTableModel classesTableModel = new DefaultTableModel();
    private MyJTable classesTable = new MyJTable(classesTableModel,0,1,new ArrayList<>());

    public ChooseGroupPage(List<ClassInfo> classes, SwitchToAddStudentTeacherToCourse switchToAddStudentTeacherToCourse,
                           String courseName, CourseGoalModel courseGoalModel, JMenuBar jMenuBar) {


        classesTableModel.addColumn("Group name");
        classesTableModel.addColumn("Number Of Students");

        classesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (ClassInfo classInfo : classes) {
            classesTableModel.addRow(new Object[] {classInfo.getClassName(), classInfo.getStudents().size(), false});
        }

        this.continueButton = new JButton();
        continueButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMenuBar(jMenuBar);
                int selectedRow = classesTable.getSelectedRow();
                ClassInfo classInfo = new ClassInfo(new ArrayList<Student>(),"No Class");
                if (selectedRow > -1) {
                    classInfo = classes.get(selectedRow);
                }
                switchToAddStudentTeacherToCourse.startAddStudentTeacherToCourse(courseName, courseGoalModel, classInfo);
            }
        });
        continueButton.setText("Continue");


        setupMenuBar(jMenuBar);

        GridBagConstraints tableConstraints = new GridBagConstraints();
        tableConstraints.fill = GridBagConstraints.BOTH;
        tableConstraints.gridy = 1;
        tableConstraints.weightx = 1.0;
        tableConstraints.weighty = 1.0;

        JTextField infoField = new JTextField();
        infoField.setEditable(false);
        infoField.setText("Choose a group to add to the course or just continue.");
        infoField.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints infoFieldConstraints = new GridBagConstraints();
        infoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        infoFieldConstraints.gridy = 0;
        infoFieldConstraints.weightx = 1.0;

        pageHolder.add(infoField,infoFieldConstraints);
        pageHolder.add(new JScrollPane(classesTable), tableConstraints);
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(continueButton);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        jMenuBar.add(continueButton);
    }

    @Override
    public JPanel getPageHolder() {
        return pageHolder;
    }
}
