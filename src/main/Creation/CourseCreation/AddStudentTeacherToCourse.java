package main.Creation.CourseCreation;

import main.DataStore.*;
import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.*;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;

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
 * Created by philip on 2016-01-13.
 *
 */
public class AddStudentTeacherToCourse implements main.Interfaces.Panel {

    private final List<ClassInfo> classIfs;
    private final String courseName;
    private final SwitchToStudentCourseGrade switchToStudentCourseGrade;
    private final CourseGradeModel courseGradeModel;
    /**
     * behöver en  courseData/info class som behöver skapas
     * en gridBagLayout som ska ha 2 fält
     * det första ett litet som inehåller alla lärare som håller i den kursen
     * den andra ska vara alla elever som ska läggas till och de som är med och en sök funktion
     */

    private PersonLexicon personLexicon;
    private JButton continueButton;
    private final RePackWindow rePackWindow;
    private final CourseGoalModel courseGoalModel;

    private GridBagLayout gridBagLayout = new GridBagLayout();
    private JPanel pageHolder = new JPanel(gridBagLayout);

    private MyTableModel studentInCourseTableModel = new MyTableModel();
    private MyTableModel studentSearchResultTableModel = new MyTableModel();
    private MyTableModel teacherInCourseTableModel = new MyTableModel();
    private MyTableModel teacherSearchResultTableModel = new MyTableModel();

    private JTable studentsInCourseTable = new JTable(studentInCourseTableModel);
    private JTable studentSearchResultTable = new JTable(studentSearchResultTableModel);
    private JTable teacherInCourseTable = new JTable(teacherInCourseTableModel);
    private JTable teacherSearchResultTable = new JTable(teacherSearchResultTableModel);

    public AddStudentTeacherToCourse(PersonLexicon personLexicon, JMenuBar jMenuBar, RePackWindow rePackWindow,
                                     String courseName, CourseGoalModel courseGoalModel, CourseGradeModel courseGradeModel,
                                     List<ClassInfo> classIfs, SwitchToStudentCourseGrade switchToStudentCourseGrade) {

        this.personLexicon = personLexicon;
        this.rePackWindow = rePackWindow;
        this.courseGoalModel = courseGoalModel;
        this.courseGradeModel = courseGradeModel;
        this.classIfs = classIfs;
        this.courseName = courseName;
        this.switchToStudentCourseGrade = switchToStudentCourseGrade;
        setupMenuButtons(jMenuBar, rePackWindow);

        GridBagLayout studentGridBagLayout = new GridBagLayout();
        GridBagConstraints studentContainerConstraints = new GridBagConstraints();
        studentContainerConstraints.gridx = 0;
        studentContainerConstraints.gridy = 0;
        studentContainerConstraints.fill = GridBagConstraints.BOTH;
        studentContainerConstraints.ipady = 50;
        studentContainerConstraints.weightx = 1.0;
        studentContainerConstraints.weighty = 3.0;
        studentContainerConstraints.gridheight = 1;
        JPanel studentLayoutContainer = new JPanel(studentGridBagLayout);

        GridBagLayout teacherGridBagLayout = new GridBagLayout();
        GridBagConstraints teacherContainerConstraints = new GridBagConstraints();
        teacherContainerConstraints.gridx = 0;
        teacherContainerConstraints.gridy = 1;
        teacherContainerConstraints.fill = GridBagConstraints.BOTH;
        teacherContainerConstraints.weightx = 1.0;
        teacherContainerConstraints.weighty = 1.0;
        JPanel teacherLayoutContainer = new JPanel(teacherGridBagLayout);

        studentSearchFieldBoxSetup(studentLayoutContainer);

        studentsInCourseBoxSetup(studentLayoutContainer);

        studentSearchResultBoxSetup(studentLayoutContainer);

        teacherSearchFieldBoxSetup(teacherLayoutContainer);

        teachersInCourseBoxSetup(teacherLayoutContainer);

        teacherSearchResultBoxSetup(teacherLayoutContainer);

        pageHolder.add(studentLayoutContainer, studentContainerConstraints);
        pageHolder.add(teacherLayoutContainer, teacherContainerConstraints);
    }

    private void setupMenuButtons(final JMenuBar jMenuBar, final RePackWindow rePackWindow) {
        this.continueButton = new JButton();
        continueButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMenuBar(jMenuBar);
                ArrayList<Teacher> teachers = new ArrayList<Teacher>();
                for (int teacherIndex = 0; teacherIndex < teacherInCourseTableModel.getRowCount(); teacherIndex++) {
                    teachers.add((Teacher) teacherInCourseTableModel.getValueAt(teacherIndex,0));
                }
                ArrayList<Student> students = new ArrayList<Student>();
                for (int studentIndex = 0; studentIndex < studentInCourseTableModel.getRowCount(); studentIndex++) {
                    students.add((Student) studentInCourseTableModel.getValueAt(studentIndex,0));
                }

                for (int i = 0; i < classIfs.size(); i++) {
                    insertNewGradeForStudentToCourse(classIfs.get(i).getStudents());
                }
                insertNewGradeForStudentToCourse(students);

                switchToStudentCourseGrade.switchToCourseGradePageAndAddCourseInfo(new CourseInfo(classIfs,students,courseName,teachers,courseGoalModel,courseGradeModel));
                rePackWindow.rePackWindow();
            }
        });
        continueButton.setText("Add to course");
        jMenuBar.add(continueButton);
    }

    private void insertNewGradeForStudentToCourse(List<Student> studentList) {
        for(int classStudentIndex = 0; classStudentIndex < studentList.size(); classStudentIndex++) {
            ArrayList<ArrayList<Integer>> grade = new ArrayList<>();
            for (int gradeRow = 0; gradeRow < courseGoalModel.getObjective().size(); gradeRow++) {
                grade.add(new ArrayList<>());
                for (int gradeCol = 0; gradeCol < courseGoalModel.getMilestone().size()*3; gradeCol++) {
                    grade.get(gradeRow).add(0);
                }
            }
            personLexicon.insertStudentGrade(studentList.get(classStudentIndex),new StudentGrade(grade),courseName);
        }
    }

    private void teacherSearchFieldBoxSetup(JPanel container) {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints teacherSearchContainerConstraints = new GridBagConstraints();
        teacherSearchContainerConstraints.gridx = 0;
        teacherSearchContainerConstraints.gridy = 0;
        teacherSearchContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
        teacherSearchContainerConstraints.weightx = 1.0;
        teacherSearchContainerConstraints.gridwidth = 2;
        teacherSearchContainerConstraints.insets = new Insets(20,0,0,0);
        JPanel teacherSearchFieldButtonContainer = new JPanel(gbl);

        JTextField teacherSearchField = new JTextField();

        teacherSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchResultTableModel(teacherSearchField, (Person p)->p.isTeacher(), teacherSearchResultTableModel, "insertUpdate");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchResultTableModel(teacherSearchField, (Person p)->p.isTeacher(), teacherSearchResultTableModel, "removeUpdate");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        GridBagConstraints searchConstraints = new GridBagConstraints();
        searchConstraints.gridx = 0;
        searchConstraints.gridy = 0;
        searchConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchConstraints.weightx = 2.0;
        searchConstraints.gridwidth = 2;
        teacherSearchFieldButtonContainer.add(teacherSearchField, searchConstraints);

        GridBagConstraints teacherSearchButtonConstraints = new GridBagConstraints();
        teacherSearchButtonConstraints.gridx = 2;
        teacherSearchButtonConstraints.gridy = 0;
        teacherSearchButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        teacherSearchButtonConstraints.weightx = 0.5;
        JButton teacherSearchButton = new JButton();
        teacherSearchButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // search for persons with name
            }
        });
        teacherSearchButton.setText("Search");
        teacherSearchFieldButtonContainer.add(teacherSearchButton, teacherSearchButtonConstraints);
        container.add(teacherSearchFieldButtonContainer, teacherSearchContainerConstraints);
    }

    private void updateSearchResultTableModel(JTextField teacherSearchField,PersonSearchFunction func, DefaultTableModel tableModel, String whichUpdate) {
        System.out.println(whichUpdate + " " + personLexicon.containsPrefix(teacherSearchField.getText()));
        Collection<Person> persons = null;
        if (personLexicon.containsPrefix(teacherSearchField.getText())) {
            persons = personLexicon.getPersonByNameAndFunction(teacherSearchField.getText(), func);
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

    private void teacherSearchResultBoxSetup(JPanel container) {
        GridBagConstraints teacherSearchConstraints = new GridBagConstraints();
        teacherSearchConstraints.gridx = 1;
        teacherSearchConstraints.gridy = 1;
        teacherSearchConstraints.fill = GridBagConstraints.BOTH;
        teacherSearchConstraints.weightx = 2.0;
        teacherSearchConstraints.weighty = 1.0;
        teacherSearchResultTableModel.addColumn("Search Result");
        teacherSearchResultTableModel.addColumn("Add teachers to course");
        container.add(new JScrollPane(teacherSearchResultTable), teacherSearchConstraints);

        GridBagConstraints addStudentConstraints = new GridBagConstraints();
        addStudentConstraints.gridx = 1;
        addStudentConstraints.gridy = 2;
        addStudentConstraints.fill = GridBagConstraints.HORIZONTAL;
        addStudentConstraints.weightx = 1.0;
        JButton addTeacher = new JButton();
        addTeacher.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = teacherSearchResultTableModel.getRowCount() -1; row > -1; row--) {
                    boolean alreadyInCourse = false;
                    if ((boolean) teacherSearchResultTableModel.getValueAt(row,1)) {
                        for (int inCourseRow = 0; inCourseRow < teacherInCourseTableModel.getRowCount(); inCourseRow++) {
                            if (((Person)teacherInCourseTableModel.getValueAt(inCourseRow,0)).getID() == ((Person)teacherSearchResultTableModel.getValueAt(row,0)).getID()) {
                                alreadyInCourse = true;
                            }
                        }
                        if (!alreadyInCourse) {
                            teacherInCourseTableModel.addRow(new Object[]{teacherSearchResultTableModel.getValueAt(row, 0), false});
                        }
                        teacherSearchResultTableModel.removeRow(row);
                    }
                }
            }
        });
        addTeacher.setText("Add Teachers");
        container.add(addTeacher, addStudentConstraints);
    }

    private void teachersInCourseBoxSetup(JPanel container) {
        GridBagConstraints removeTeacherConstraints = new GridBagConstraints();
        removeTeacherConstraints.gridx = 0;
        removeTeacherConstraints.gridy = 2;
        removeTeacherConstraints.fill = GridBagConstraints.HORIZONTAL;
        removeTeacherConstraints.weightx = 1.0;
        JButton removeTeacher = new JButton();
        removeTeacher.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = teacherInCourseTableModel.getRowCount() -1; row > -1; row--) {
                    if ((boolean) teacherInCourseTableModel.getValueAt(row,1)) {
                        teacherInCourseTableModel.removeRow(row);
                    }
                }
            }
        });
        removeTeacher.setText("Remove teachers");
        container.add(removeTeacher, removeTeacherConstraints);


        GridBagConstraints teacherTableConstraints = new GridBagConstraints();
        teacherTableConstraints.gridx = 0;
        teacherTableConstraints.gridy = 1;
        teacherTableConstraints.fill = GridBagConstraints.BOTH;
        teacherTableConstraints.weightx = 1.0;
        teacherTableConstraints.weighty = 1.0;
        teacherInCourseTableModel.addColumn("Teachers in course");
        teacherInCourseTableModel.addColumn("RemoveBox");
        container.add(new JScrollPane(teacherInCourseTable), teacherTableConstraints);
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
                updateSearchResultTableModel(studentSearchField,(Person p)->!p.isTeacher(), studentSearchResultTableModel, "insertUpdate");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchResultTableModel(studentSearchField,(Person p)->!p.isTeacher(), studentSearchResultTableModel, "removeUpdate");
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
        removeStudentConstraints.gridy = 2;
        removeStudentConstraints.fill = GridBagConstraints.HORIZONTAL;
        removeStudentConstraints.weightx = 1.0;
        JButton removeStudents = new JButton();
        removeStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = studentInCourseTableModel.getRowCount() -1; row > -1; row--) {
                    if ((boolean) studentInCourseTableModel.getValueAt(row,1)) {
                        studentInCourseTableModel.removeRow(row);
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
        studentInCourseTableModel.addColumn("Students in course");
        studentInCourseTableModel.addColumn("RemoveBox");
        container.add(new JScrollPane(studentsInCourseTable), studentTableConstraints);
    }

    private void studentSearchResultBoxSetup(JPanel container) {
        GridBagConstraints studentSearchConstraints = new GridBagConstraints();
        studentSearchConstraints.gridx = 1;
        studentSearchConstraints.gridy = 1;
        studentSearchConstraints.fill = GridBagConstraints.BOTH;
        studentSearchConstraints.weightx = 2.0;
        studentSearchConstraints.weighty = 1.0;
        studentSearchResultTableModel.addColumn("Search Result");
        studentSearchResultTableModel.addColumn("Add students to course");
        container.add(new JScrollPane(studentSearchResultTable), studentSearchConstraints);

        GridBagConstraints addStudentConstraints = new GridBagConstraints();
        addStudentConstraints.gridx = 1;
        addStudentConstraints.gridy = 2;
        addStudentConstraints.fill = GridBagConstraints.HORIZONTAL;
        addStudentConstraints.weightx = 1.0;
        JButton addStudents = new JButton();
        addStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = studentSearchResultTableModel.getRowCount() -1; row > -1; row--) {
                    boolean alreadyInCourse = false;
                    if ((boolean) studentSearchResultTableModel.getValueAt(row,1)) {
                        if (classIfs == null || !containsStudent((Student) studentSearchResultTableModel.getValueAt(row, 0),classIfs)){
                            for (int inCourseRow = 0; inCourseRow < studentInCourseTableModel.getRowCount(); inCourseRow++) {
                                if (((Person) studentInCourseTableModel.getValueAt(inCourseRow, 0)).getID() == ((Person) studentSearchResultTableModel.getValueAt(row, 0)).getID()) {
                                    alreadyInCourse = true;
                                }
                            }
                        if (!alreadyInCourse) {
                            studentInCourseTableModel.addRow(new Object[]{studentSearchResultTableModel.getValueAt(row, 0), false});
                        }
                        studentSearchResultTableModel.removeRow(row);
                        }
                    }
                }
            }
        });
        addStudents.setText("Add Students");
        container.add(addStudents, addStudentConstraints);
    }

    private boolean containsStudent(Student student, List<ClassInfo> classInfos) {
        for (int i = 0; i < classInfos.size(); i++) {
            if (classInfos.get(i).getStudents().contains(student)) {
                return true;
            }
        }
        return false;
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(continueButton);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        setupMenuButtons(jMenuBar, rePackWindow);
    }
}