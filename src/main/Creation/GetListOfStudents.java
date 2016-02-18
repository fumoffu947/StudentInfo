package main.Creation;

import main.Creation.CourseCreation.MyTableModel;
import main.DataStore.ClassInfo;
import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.Student;
import main.DataStore.StudentCourseGrade;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.AddToYearHolderPage;
import main.Interfaces.InterfaceDataTransfer.SwitchToGivenPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by phili on 2016-02-17.
 */
public class GetListOfStudents implements main.Interfaces.Panel {

    private final RePackWindow rePackWindow;
    private final SwitchToGivenPanel switchToGivenPanel;
    private final StudentCourseGrade studentCourseGrade;
    private JButton chooseStudents;
    private List<Student> studentGroup = new ArrayList<>();
    private PersonLexicon personLexicon;

    GridBagLayout gridBagLayout = new GridBagLayout();
    private JPanel pageHolder = new JPanel(gridBagLayout);

    private MyTableModel searchResultTableModel = new MyTableModel();
    private MyTableModel inClassTableModel = new MyTableModel();

    private JTable inCourseTable = new JTable(inClassTableModel);
    private JTable searchResultTable = new JTable(searchResultTableModel);

    public GetListOfStudents(PersonLexicon personLexicon, RePackWindow rePackWindow,
                             JMenuBar jMenuBar, SwitchToGivenPanel switchToGivenPanel, StudentCourseGrade studentCourseGrade) {
        this.personLexicon = personLexicon;
        this.rePackWindow = rePackWindow;
        this.switchToGivenPanel = switchToGivenPanel;
        this.studentCourseGrade = studentCourseGrade;

        setupMenuButtons(jMenuBar);

        studentSearchFieldBoxSetup(pageHolder);
        studentSearchResultBoxSetup(pageHolder);
        studentsInCourseBoxSetup(pageHolder);

        rePackWindow.rePackWindow();
    }

    private void setupMenuButtons(final JMenuBar jMenuBar) {
        this.chooseStudents = new JButton();
        chooseStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < inClassTableModel.getRowCount(); row++) {
                    studentGroup.add((Student) inClassTableModel.getValueAt(row,0));
                }
                if (!studentGroup.isEmpty()) {
                    studentCourseGrade.addStudentsToCourse(studentGroup);
                    switchToGivenPanel.switchTo(studentCourseGrade);
                }

            }
        });
        chooseStudents.setText("Choose students");
        jMenuBar.add(chooseStudents);
        rePackWindow.rePackWindow();
    }

    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(chooseStudents);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        setupMenuButtons(jMenuBar);
    }

    private void updateSearchResultTableModel(JTextField teacherSearchField, DefaultTableModel tableModel, String whichUpdate) {
        System.out.println(whichUpdate + " " + personLexicon.containsPrefix(teacherSearchField.getText()));
        Collection<Person> persons = null;
        if (personLexicon.containsPrefix(teacherSearchField.getText())) {
            persons = personLexicon.getPersonsByName(teacherSearchField.getText());
        }
        if (persons != null && !persons.isEmpty()) {
            clearTableModel(tableModel);
            for (Person p : persons) {
                tableModel.addRow(new Object[] {p , false});
            }
        }else {
            clearTableModel(tableModel);
        }
    }

    private void clearTableModel(DefaultTableModel tableModel) {
        for (int tableRow = tableModel.getRowCount()-1; tableRow > -1; tableRow--) {
            tableModel.removeRow(tableRow);
        }
    }

    private void studentSearchFieldBoxSetup(JPanel container) {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints studentSearchContainerConstraints = new GridBagConstraints();
        studentSearchContainerConstraints.gridx = 0;
        studentSearchContainerConstraints.gridy = 0;
        studentSearchContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
        studentSearchContainerConstraints.weightx = 1.0;
        studentSearchContainerConstraints.gridwidth = 2;
        JPanel studentSearchFieldButtonContainer = new JPanel(gbl);

        JTextField studentSearchField = new JTextField();
        studentSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchResultTableModel(studentSearchField, searchResultTableModel, "insertUpdate");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchResultTableModel(studentSearchField, searchResultTableModel, "removeUpdate");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        GridBagConstraints studentSearchConstraints = new GridBagConstraints();
        studentSearchConstraints.gridx = 0;
        studentSearchConstraints.gridy = 0;
        studentSearchConstraints.fill = GridBagConstraints.HORIZONTAL;
        studentSearchConstraints.weightx = 2.0;
        studentSearchConstraints.gridwidth = 2;
        studentSearchFieldButtonContainer.add(studentSearchField, studentSearchConstraints);

        GridBagConstraints studentSearchButtonConstraints = new GridBagConstraints();
        studentSearchButtonConstraints.gridx = 2;
        studentSearchButtonConstraints.gridy = 0;
        studentSearchButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        studentSearchButtonConstraints.weightx = 0.5;
        JButton studentSearchButton = new JButton();
        studentSearchButton.setAction(new AbstractAction() { // ################ fundera på att att bort den
            @Override
            public void actionPerformed(ActionEvent e) {
                // search for persons with name
            }
        });
        studentSearchButton.setText("Search");
        studentSearchFieldButtonContainer.add(studentSearchButton, studentSearchButtonConstraints);
        container.add(studentSearchFieldButtonContainer, studentSearchContainerConstraints);
    }

    private void studentsInCourseBoxSetup(JPanel container) {
        GridBagConstraints removeStudentConstraints = new GridBagConstraints();
        removeStudentConstraints.gridx = 0;
        removeStudentConstraints.gridy = 3;
        removeStudentConstraints.fill = GridBagConstraints.HORIZONTAL;
        removeStudentConstraints.weightx = 1.0;
        JButton removeStudents = new JButton();
        removeStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = inClassTableModel.getRowCount() -1; row > -1; row--) {
                    if ((boolean) inClassTableModel.getValueAt(row,1)) {
                        inClassTableModel.removeRow(row);
                    }
                }
            }
        });
        removeStudents.setText("Remove Students");
        container.add(removeStudents, removeStudentConstraints);


        GridBagConstraints studentTableConstraints = new GridBagConstraints();
        studentTableConstraints.gridx = 0;
        studentTableConstraints.gridy = 1;
        studentTableConstraints.fill = GridBagConstraints.BOTH;
        studentTableConstraints.weightx = 1.0;
        studentTableConstraints.weighty = 1.0;
        inClassTableModel.addColumn("Students in class");
        inClassTableModel.addColumn("RemoveBox");
        container.add(new JScrollPane(inCourseTable), studentTableConstraints);

    }

    private void studentSearchResultBoxSetup(JPanel container) {
        GridBagConstraints studentSearchConstraints = new GridBagConstraints();
        studentSearchConstraints.gridx = 1;
        studentSearchConstraints.gridy = 1;
        studentSearchConstraints.fill = GridBagConstraints.BOTH;
        studentSearchConstraints.weightx = 2.0;
        studentSearchConstraints.weighty = 1.0;
        studentSearchConstraints.gridheight = 2;
        searchResultTableModel.addColumn("Search Result");
        searchResultTableModel.addColumn("Add students to class");
        container.add(new JScrollPane(searchResultTable), studentSearchConstraints);

        GridBagConstraints addStudentConstraints = new GridBagConstraints();
        addStudentConstraints.gridx = 1;
        addStudentConstraints.gridy = 3;
        addStudentConstraints.fill = GridBagConstraints.HORIZONTAL;
        addStudentConstraints.weightx = 1.0;
        JButton addStudents = new JButton();
        addStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = searchResultTableModel.getRowCount() -1; row > -1; row--) {
                    boolean alreadyInCourse = false;
                    if ((boolean) searchResultTableModel.getValueAt(row,1)) {
                        for (int inClassRow = 0; inClassRow < inClassTableModel.getRowCount(); inClassRow++) {
                            if (((Person)inClassTableModel.getValueAt(inClassRow,0)).getID() == ((Person)searchResultTableModel.getValueAt(row,0)).getID()) {
                                alreadyInCourse = true;
                            }
                        }
                        if (!alreadyInCourse && !((Person) searchResultTableModel.getValueAt(row,0)).isTeacher()) {
                            inClassTableModel.addRow(new Object[]{searchResultTableModel.getValueAt(row, 0), false});
                        }
                        searchResultTableModel.removeRow(row);
                    }
                }
            }
        });
        addStudents.setText("Add Students");
        container.add(addStudents, addStudentConstraints);
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }
}
