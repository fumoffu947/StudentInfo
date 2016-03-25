package main.DataStore;

import main.Creation.CourseCreation.MyTableModel;
import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.StartGetListOfStudents;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Created by phili on 2016-01-16.
 * this page is the page where a courses Students and grade for the students are shown
 */
public class StudentCourseGrade implements main.Interfaces.Panel {

    private final StudentCourseGrade thisClass;

    private final PersonLexicon personLexicon;
    private final RePackWindow rePackWindow;
    private final StartGetListOfStudents startGetListOfStudents;
    private CourseInfo courseInfo;

    private boolean contentChanged = true;
    private int currentStudentPageIndex = 0;

    private List<JPanel> studentPages = new ArrayList<>();

    private GridBagLayout gridBagLayout = new GridBagLayout();
    private JPanel pageHolder = new JPanel(gridBagLayout);
    private JPanel studentGradePage = new JPanel(new BorderLayout());
    private JPanel summaryTableContainer = new JPanel(new BorderLayout());

    private MyTableModel studentListTableModel = new MyTableModel();

    private MyJTable studentList = new MyJTable(studentListTableModel,0,0, new ArrayList<>());
    private JButton toggleRemoveStudents;
    private JButton removeStudentsButton;
    private JButton acquireNewStudents;
    private boolean changePage = true;

    public StudentCourseGrade(CourseInfo courseInfo, JMenuBar jMenuBar, PersonLexicon personLexicon, RePackWindow rePackWindow, StartGetListOfStudents startGetListOfStudents) {
        this.courseInfo = courseInfo;
        this.personLexicon = personLexicon;
        this.rePackWindow = rePackWindow;
        this.startGetListOfStudents = startGetListOfStudents;


        // placemant of the selectList of the student to the leaft
        GridBagConstraints studentListConstraints = new GridBagConstraints();
        studentListConstraints.gridx = 0;
        studentListConstraints.weightx = 0.5;
        studentListConstraints.weighty = 1.0;
        studentListConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(new JScrollPane(studentList) , studentListConstraints);

        // placement of the gradepage that is swaped out between each student selection
        GridBagConstraints studentPageGradeConstraints = new GridBagConstraints();
        studentPageGradeConstraints.gridx = 1;
        studentPageGradeConstraints.weightx = 3.0;
        studentPageGradeConstraints.weighty = 1.0;
        studentPageGradeConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(studentGradePage, studentPageGradeConstraints);

        // the acutal list of student to select and the selection action
        studentListTableModel.addColumn("Pages");
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (e.getValueIsAdjusting() && lsm.isSelectedIndex(i)) {
                        System.out.println(studentListTableModel.getValueAt(i,0));
                        System.out.println(i);
                        studentGradePage.removeAll();
                        studentGradePage.add(studentPages.get(i));
                        currentStudentPageIndex = i;
                        rePackWindow.rePackWindow();
                    }
                }
            }
        });

        studentListTableModel.addRow(new Object[] {"Summary"});

        // adds the summary page to the studentPage
        setupSummaryPage();

        // add separate pages to all the students in the course
        addListOfStudent(courseInfo.getClassInfo().getStudents());
        addListOfStudent(courseInfo.getOtherEnlistedStudents());


        //create the button to remove students
        removeStudentsButton = new JButton();
        removeStudentsButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int row = studentList.getModel().getRowCount()-1; row > 0;row--) {
                    if ((boolean) studentList.getValueAt(row,1)) {
                        if (row - 1 < courseInfo.getClassInfo().getStudents().size()) {
                            System.out.println(courseInfo.removeStudent((Student) studentList.getValueAt(row,0)));
                        } else {
                            System.out.println(courseInfo.removeOtherEnlistedStudent((Student) studentList.getValueAt(row,0)));
                        }
                        contentChanged = true;
                        setupSummaryPage();
                    }
                }
                reMakeStudentListSelecter(courseInfo);


            }
        });
        removeStudentsButton.setText("Remove Students");
        setupMenuBar(jMenuBar);
        rePackWindow.rePackWindow();

        this.thisClass = this;
    }

    private void reMakeStudentListSelecter(CourseInfo courseInfo) {
        // redo the entire table model for the STudentList because i dont knwo how to remove a col rom a model or get the table to not recreate it from the model and to then apint the checkboxex correct
        MyTableModel tmp = new MyTableModel();
        tmp.addColumn("Pages");
        tmp.addRow(new Object[] {"Summary"});
        for (int studentIndex = 0; studentIndex < courseInfo.getClassInfo().getStudents().size(); studentIndex++) {
            // add the student to the userSelection table on the left
            tmp.addRow(new Object[]{courseInfo.getClassInfo().getStudents().get(studentIndex)});
        }
        for (int studentIndex = 0; studentIndex < courseInfo.getOtherEnlistedStudents().size(); studentIndex++) {
            // add the student to the userSelection table on the left
            tmp.addRow(new Object[]{courseInfo.getOtherEnlistedStudents().get(studentIndex)});
        }
        studentListTableModel = tmp;
        studentList.setModel(tmp);
    }

    private void addListOfStudent(List<Student> givenStudents) {
        // goes through the givenStudents and add separate pages for each one of them
        for (int studentIndex = 0; studentIndex < givenStudents.size(); studentIndex++) {
            // add the student to the userSelection table on the left
            studentListTableModel.addRow(new Object[] {givenStudents.get(studentIndex)});

            JPanel studentGradeContainer = new JPanel(new BorderLayout());
            DefaultTableModel studentTableModel = new DefaultTableModel();
            MyJTable studentTable = new MyJTable(studentTableModel,0,0,new ArrayList<>(Collections.singletonList(courseInfo.getCourseGoalModel().getMilestone().size() + 1)));
            studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(givenStudents.get(studentIndex), courseInfo.getCourseName());

            studentTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    Vector<Object> vector = (Vector<Object>) studentTableModel.getDataVector().get(studentTable.getSelectedRow());
                    int sum = 0;
                    for (int index = 1; index < vector.size()-1; index++) {
                        if (vector.get(index).getClass().equals(String.class)) {
                            sum += Integer.parseInt((String) vector.get(index));
                        }
                        else if (vector.get(index).getClass().equals(Integer.class)) {
                            sum += (Integer) vector.get(index);
                        }
                    }
                    // parse the new value just typed in to just do i once
                    int newValue = 0;
                    if (vector.get(studentTable.getSelectedColumn()).getClass().equals(String.class)) {
                        newValue = Integer.parseInt((String) vector.get(studentTable.getSelectedColumn()));
                    }
                    else if (vector.get(studentTable.getSelectedColumn()).getClass().equals(Integer.class)) {
                        newValue = (Integer) vector.get(studentTable.getSelectedColumn());
                    }

                    studentTableModel.setValueAt(sum,studentTable.getSelectedRow(), courseInfo.getCourseGoalModel().getMilestone().size() + 1);

                    // change the grade in the personLexicon
                    if (currentStudentPageIndex - 1 < courseInfo.getClassInfo().getStudents().size()) {
                        StudentGrade grade = personLexicon.getCourseGradeByPerson(courseInfo.getClassInfo().getStudents().get(currentStudentPageIndex-1), courseInfo.getCourseName());
                        grade.setValueAt(studentTable.getSelectedRow(), studentTable.getSelectedColumn()-1, newValue);
                        personLexicon.insertStudentGrade(courseInfo.getClassInfo().getStudents().get(currentStudentPageIndex-1), grade, courseInfo.getCourseName());
                    } else {
                        StudentGrade grade = personLexicon.getCourseGradeByPerson(courseInfo.getOtherEnlistedStudents().get(currentStudentPageIndex-1), courseInfo.getCourseName());
                        grade.setValueAt(studentTable.getSelectedRow(), studentTable.getSelectedColumn()-1, newValue);
                        personLexicon.insertStudentGrade(courseInfo.getOtherEnlistedStudents().get(currentStudentPageIndex-1), grade, courseInfo.getCourseName());
                    }

                    contentChanged = true;
                    changePage = false;
                    setupSummaryPage();
                }

                @Override
                public void editingCanceled(ChangeEvent e) {

                }
            });
            // adds the first column
            studentTableModel.addColumn("Goals");
            // adds all the columns of partGoals to this student table
            for (int colIndex = 0; colIndex < courseInfo.getCourseGoalModel().getMilestone().size(); colIndex++) {
                studentTableModel.addColumn(courseInfo.getCourseGoalModel().getMilestone().get(colIndex));
            }
            studentTableModel.addColumn("Summary");
            //adds all the rows of data to this table, which is gotten from this students StudentGrade from personLexicon
            // and adds all of then to an ObjectArray which then is the full data row
            for (int gradeRow = 0; gradeRow < courseInfo.getCourseGoalModel().getObjective().size(); gradeRow++) {
                Object[] grade = studentGrade.getGrades().get(gradeRow).toArray();
                int goalSum = studentGrade.getGrades().get(gradeRow).stream().mapToInt(Integer::intValue).sum();
                Object[] rowData = new Object[grade.length+2];
                rowData[grade.length+1] = goalSum;
                rowData[0] = courseInfo.getCourseGoalModel().getObjective().get(gradeRow);
                // adds the scores from the grade to the current row ObjectArray
                for (int index = 1; index < grade.length+1; index++) {
                    rowData[index] = grade[index-1];
                }
                studentTableModel.addRow(rowData);
            }

            studentGradeContainer.add(new JScrollPane(studentTable));
            studentPages.add(studentGradeContainer);
        }
    }

    /**
     * NEEEEEEEEEEEEEEEEEEEEEED to change so that other enlisted student also is with this
     */
    private void setupSummaryPage() {
        if (contentChanged) {
            summaryTableContainer.removeAll();
            DefaultTableModel summaryTableModel = new DefaultTableModel();
            MyJTable summaryTable = new MyJTable(summaryTableModel, 0, courseInfo.getClassInfo().getStudents().size() + courseInfo.getOtherEnlistedStudents().size(), new ArrayList<>());

            summaryTableModel.addColumn("Students");

            char[] gradeLevelArrayChar = new char[3];
            gradeLevelArrayChar[0] = 'E';
            gradeLevelArrayChar[1] = 'C';
            gradeLevelArrayChar[2] = 'A';

            for (int gradeLevel = 0; gradeLevel < 3; gradeLevel++) {
                for (int goalModelRow = 0; goalModelRow < courseInfo.getCourseGoalModel().getObjective().size(); goalModelRow++) {
                    summaryTableModel.addColumn(courseInfo.getCourseGoalModel().getObjective().get(goalModelRow)+" "+gradeLevelArrayChar[gradeLevel]);
                }
            }
            //summaryTableModel.addColumn("Goals", courseInfo.getCourseGoalModel().getObjective().toArray());

            // goes through the students grades and sums them for each goal and show them in the summaryPage
            summaryTableAddStudent(summaryTableModel, courseInfo.getClassInfo().getStudents());
            summaryTableAddStudent(summaryTableModel, courseInfo.getOtherEnlistedStudents());

            summaryTableContainer.add(new JScrollPane(summaryTable));
            // if it is the first time the constructor is run
            if (studentPages.size() != 0) {
                studentPages.set(0, summaryTableContainer);
            }else {
                studentPages.add(summaryTableContainer);
            }

            // if a change has been made to a student record
            if (changePage) {
                studentGradePage.removeAll();
                studentGradePage.add(summaryTableContainer);
                rePackWindow.rePackWindow();
            }

            changePage = false;
            contentChanged = false;
        }
    }

    public void addStudentsToCourse(List<Student> students) {
        courseInfo.getOtherEnlistedStudents().addAll(students);
        changePage = true;
        contentChanged = true;
        setupSummaryPage();
        reMakeStudentListSelecter(courseInfo);
    }

    //used when the summaryPage is setup
    private void summaryTableAddStudent(DefaultTableModel summaryTableModel, List<Student> currentStudentList) {
        for (int studentIndex = 0; studentIndex < currentStudentList.size(); studentIndex++) {
            ArrayList<Object> columnData = new ArrayList<>();
            columnData.add(currentStudentList.get(studentIndex));
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(currentStudentList.get(studentIndex), courseInfo.getCourseName());
            //sums each grade and adds to an array to be the columnDataObject
            for (int gradeLevel = 0; gradeLevel < 3; gradeLevel++) {
                for (int gradeRow = 0; gradeRow < studentGrade.getGrades().size(); gradeRow++) {
                    int currentGradeLevelSum = 0;
                    for (int gradeCol = (studentGrade.getGrades().get(0).size()/3)*gradeLevel; gradeCol < (studentGrade.getGrades().get(0).size()/3)*(gradeLevel+1); gradeCol++) {
                        currentGradeLevelSum += studentGrade.getGrades().get(gradeRow).get(gradeCol);
                    }
                    columnData.add(currentGradeLevelSum);
                }
            }

            summaryTableModel.addRow(columnData.toArray());
        }
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(toggleRemoveStudents);
        jMenuBar.remove(acquireNewStudents);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        this.toggleRemoveStudents = new JButton();
        toggleRemoveStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tableModel = (DefaultTableModel) studentList.getModel();
                ArrayList<Boolean> columnFill = new ArrayList<>();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    columnFill.add(false);
                }

                tableModel.addColumn("Students To Remove", columnFill.toArray());

                GridBagConstraints removeStudentConstraints = new GridBagConstraints();
                removeStudentConstraints.gridx = 0;
                removeStudentConstraints.gridy = 1;
                removeStudentConstraints.weightx  = 1.0;
                removeStudentConstraints.weighty = 5.0;
                pageHolder.add(removeStudentsButton, removeStudentConstraints);
                rePackWindow.rePackWindow();
            }
        });
        toggleRemoveStudents.setText("Toggle Remove Students");

        this.acquireNewStudents = new JButton();
        acquireNewStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGetListOfStudents.startGetStudents(thisClass);
            }
        });
        acquireNewStudents.setText("Add students");

        jMenuBar.add(toggleRemoveStudents);
        jMenuBar.add(acquireNewStudents);
    }

    public CourseInfo getCourseInfo() {
        return  courseInfo;
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }
}

