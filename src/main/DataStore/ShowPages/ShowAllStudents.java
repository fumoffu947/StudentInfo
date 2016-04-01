package main.DataStore.ShowPages;

import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.MyJTable;
import main.DataStore.SettingsLoader;
import main.DataStore.Student;
import main.Interfaces.InterfaceDataTransfer.CourseInfoTransfer;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;
import main.Interfaces.Panel;
import main.Interfaces.Person;
import main.Interfaces.RePackWindow;
import sun.awt.image.BufferedImageGraphicsConfig;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Created by phili on 2016-03-29.
 */
public class ShowAllStudents implements Panel {

    private JPanel pageHolder = new JPanel(new GridBagLayout());

    private DefaultTableModel studentListTableModel = new DefaultTableModel();
    private MyJTable studentListTable = new MyJTable(studentListTableModel,0,1,new ArrayList<>());

    private JPanel currentStudentPage = new JPanel(new BorderLayout());

    private JButton removeSelectedStudents;

    private List<JPanel> studentPages = new ArrayList<>();

    public ShowAllStudents(JMenuBar jMenuBar, PersonLexicon personLexicon, RePackWindow rePackWindow,
                           CourseInfoTransfer courseInfoTransfer, SwitchToStudentCourseGrade switchToStudentCourseGrade, SettingsLoader settingsLoader) {

        GridBagConstraints studentListConstraints = new GridBagConstraints();
        studentListConstraints.gridx = 0;
        studentListConstraints.weightx = 0.5;
        studentListConstraints.weighty = 1.0;
        studentListConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(new JScrollPane(studentListTable), studentListConstraints);

        GridBagConstraints currentStudentPageConstraints = new GridBagConstraints();
        currentStudentPageConstraints.gridx = 1;
        currentStudentPageConstraints.weightx = 3.0;
        currentStudentPageConstraints.weighty = 1.0;
        currentStudentPageConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(currentStudentPage, currentStudentPageConstraints);

        studentListTableModel.addColumn("Students");

        this.removeSelectedStudents = new JButton();
        removeSelectedStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person p = (Student) studentListTable.getValueAt(studentListTable.getSelectedRow(), 0);
                studentListTableModel.removeRow(studentListTable.getSelectedRow());
                personLexicon.removePerson(p,settingsLoader);
            }
        });
        removeSelectedStudents.setText("Remove Selected Student");


        setupStudentPages(personLexicon, courseInfoTransfer, switchToStudentCourseGrade);

        studentListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (e.getValueIsAdjusting() && lsm.isSelectedIndex(i)) {
                        currentStudentPage.removeAll();
                        currentStudentPage.add(studentPages.get(i));
                        rePackWindow.rePackWindow();
                    }
                }
            }
        });

        if (!studentPages.isEmpty()) {
            currentStudentPage.add(studentPages.get(0));
        }

        System.out.println("number of rows in studentList: "+studentListTable.getRowCount() + " number of pages in the list of pages: "+studentPages.size()+"\n");
        setupMenuBar(jMenuBar);
        rePackWindow.rePackWindow();
    }


    private void setupStudentPages(PersonLexicon personLexicon, CourseInfoTransfer courseInfoTransfer, SwitchToStudentCourseGrade switchToStudentCourseGrade) {
        List<Student> students = personLexicon.getAllStudents();
        for (Student s : students) {
            studentListTableModel.addRow(new Object[] {s});
            List<String> studentsCourses = personLexicon.getCourseNamesForPerson(s);
            DefaultTableModel currentTableModel = new DefaultTableModel();
            MyJTable currentTable = new MyJTable(currentTableModel,0,2,new ArrayList<>());
            currentTableModel.addColumn("Courses", studentsCourses.toArray());

            currentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            currentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                    int minIndex = lsm.getMinSelectionIndex();
                    int maxIndex = lsm.getMaxSelectionIndex();
                    for (int i = minIndex; i <= maxIndex; i++) {
                        if (e.getValueIsAdjusting() && lsm.isSelectedIndex(i)) {
                            currentTable.clearSelection();
                            switchToStudentCourseGrade.switchToCourseGradePage(courseInfoTransfer.getCourseInfoByName((String) currentTable.getValueAt(i,0)));
                        }
                    }
                }
            });

            JPanel currentStudentPage = new JPanel(new GridBagLayout());
            GridBagConstraints studentInfoConstraints = new GridBagConstraints();
            studentInfoConstraints.gridy = 0;
            studentInfoConstraints.weighty = 0.5;
            studentInfoConstraints.weightx = 1.0;
            studentInfoConstraints.fill = GridBagConstraints.HORIZONTAL;
            currentStudentPage.add(s.getPageHolder(),studentInfoConstraints);

            GridBagConstraints tabelContstrain = new GridBagConstraints();
            tabelContstrain.gridy = 1;
            tabelContstrain.weightx = 1.0;
            tabelContstrain.weighty = 3.0;
            tabelContstrain.fill = GridBagConstraints.BOTH;
            currentStudentPage.add(new JScrollPane(currentTable), tabelContstrain);

            studentPages.add(currentStudentPage);
        }
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(removeSelectedStudents);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        jMenuBar.add(removeSelectedStudents);
    }

    @Override
    public JPanel getPageHolder() {
        return pageHolder;
    }
}
