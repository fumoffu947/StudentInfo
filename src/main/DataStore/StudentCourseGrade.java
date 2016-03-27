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
    private List<Boolean> upToDateList = new ArrayList<>();
    private List<Integer> translationList = new ArrayList<>();

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
    private char[] gradeLevelArrayChar;

    public StudentCourseGrade(CourseInfo courseInfo, JMenuBar jMenuBar, PersonLexicon personLexicon, RePackWindow rePackWindow, StartGetListOfStudents startGetListOfStudents) {
        this.courseInfo = courseInfo;
        this.personLexicon = personLexicon;
        this.rePackWindow = rePackWindow;
        this.startGetListOfStudents = startGetListOfStudents;
        this.gradeLevelArrayChar = new char[3];
        gradeLevelArrayChar[0] = 'E';
        gradeLevelArrayChar[1] = 'C';
        gradeLevelArrayChar[2] = 'A';


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
                        ClassInfoListIndexHolder indexHolder = selectListIndexTranslator(i);
                        if (indexHolder.totalIndex == -1) {
                            return;
                        }
                        if (!upToDateList.get(indexHolder.totalIndex) && indexHolder.totalIndex != 0) {
                            JTable table = (JTable) ((JScrollPane)((JPanel)studentPages.get(indexHolder.totalIndex).getComponents()[0]).getComponents()[0]).getComponents()[0];
                            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                            for (int row = 0; row < courseInfo.getCourseGoalModel().getMaxPoits().size(); row++) {
                                for (int col = 1; col < courseInfo.getCourseGoalModel().getMaxPoits().get(row).size()+1; col++) {
                                    tableModel.setValueAt(courseInfo.getCourseGoalModel().getMaxPoits().get(row).get(col-1),row*2+1,col);
                                }
                            }
                        }
                        studentGradePage.removeAll();
                        studentGradePage.add(studentPages.get(indexHolder.totalIndex));
                        currentStudentPageIndex = i;
                        rePackWindow.rePackWindow();
                    }
                }
            }
        });

        studentListTableModel.addRow(new Object[] {"Summary"});

        // adds the summary page to the studentPage
        setupSummaryPage();

        // add a bool for the summary page
        upToDateList.add(true);

        // add separate pages to all the students in the course
        for(ClassInfo classInfo : courseInfo.getClassInfoList()) {
            if (!classInfo.getClassName().equals("No Class")) {
                studentListTableModel.addRow(new Object[]{classInfo.getClassName()});
                addListOfStudent(classInfo.getStudents());
                for (int i = 0; i < classInfo.getStudents().size(); i++) {
                    upToDateList.add(true);
                }
                if (translationList.isEmpty()) {
                    translationList.add(classInfo.getStudents().size() + 1);
                }else {
                    translationList.add(translationList.get(translationList.size()-1)+ classInfo.getStudents().size() + 1);
                }
            }

        }
        studentListTableModel.addRow(new Object[] {"Other Students"});
        addListOfStudent(courseInfo.getOtherEnlistedStudents());
        for (int i = 0; i < courseInfo.getOtherEnlistedStudents().size(); i++) {
            upToDateList.add(true);
        } /*
        if (translationList.isEmpty()) {
            translationList.add(courseInfo.getOtherEnlistedStudents().size() + 1);
        } else {
            translationList.add(translationList.get(translationList.size()-1)+courseInfo.getOtherEnlistedStudents().size() + 1);
        }*/


        //create the button to remove students
        removeStudentsButton = new JButton();
        removeStudentsButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int row = studentList.getModel().getRowCount()-1; row > 0;row--) {
                    if ((boolean) studentList.getValueAt(row,1)) {
                        ClassInfoListIndexHolder indexHolder = selectListIndexTranslator(row);
                        if (!indexHolder.otherStudentList) {
                            System.out.println(courseInfo.removeStudent((Student) studentList.getValueAt(row,0)));
                        } else {
                            System.out.println(courseInfo.removeOtherEnlistedStudent((Student) studentList.getValueAt(row,0)));
                        }
                        contentChanged = true;
                        setupSummaryPage();
                    }
                }
                reMakeStudentListSelector(courseInfo);


            }
        });
        removeStudentsButton.setText("Remove Students");
        setupMenuBar(jMenuBar);
        rePackWindow.rePackWindow();

        this.thisClass = this;
    }

    /**
     * sets all the other booleans for knowing if given pos maxPoints is up to date
     * @param selfPos
     * it's the true pos of the list not pos of current selected in the selection list
     */
    private void resetUpdateToOther(int selfPos) {
        if (upToDateList.get(selfPos)) {
            for (int index = 0; index < upToDateList.size(); index++) {
                if (index != selfPos) {
                    upToDateList.set(index, false);
                }
            }
        }else {
            for (int i = 0; i < studentPages.size(); i++) {
                if (!upToDateList.get(i)) {
                    JTable table = (JTable) ((JScrollPane) ((JPanel) studentPages.get(i).getComponents()[0]).getComponents()[0]).getComponents()[0];
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    for (int row = 0; row < courseInfo.getCourseGoalModel().getMaxPoits().size(); row++) {
                        for (int col = 1; col < courseInfo.getCourseGoalModel().getMaxPoits().get(row).size() + 1; col++) {
                            tableModel.setValueAt(courseInfo.getCourseGoalModel().getMaxPoits().get(row).get(col - 1), row*2+1, col);
                        }
                    }

                }
                upToDateList.set(i, true);
            }
        }
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!need to add bool to say if it is on an class name or other name
     * translates a pos in the student list to an list index to the classes and an index into that list and
     * a boolean to say if it is an other student or not and create the total index
     * @param index
     * the index wanting to translate
     * @return
     * return index in class list, index into the list, bool for other students, toatal index of all students
     */
    private ClassInfoListIndexHolder selectListIndexTranslator(int index) {
        if (index == 0) {
            return new ClassInfoListIndexHolder(-1,index,false,0);
        }
        //special case for the first case of when the user presses on the name of the first class or otherClass
        if (index == 1) {
            return new ClassInfoListIndexHolder(-1,-1,false,-1);
        }
        for (int i = 0; i < translationList.size(); i++) {
            if (index <= translationList.get(i)) {
                if (i == 0) {
                    // spacial case for the first list entry (i = 0)
                    return new ClassInfoListIndexHolder(i,index-2,false,index-i-1);
                } else {
                    // clicked on an class name
                    if (index == translationList.get(i-1)+1) {
                        return new ClassInfoListIndexHolder(-1,-1,false,-1);
                    }
                    return new ClassInfoListIndexHolder(index-translationList.get(i-1)-1,i,false,index-i-1);
                }
            }
        }

        int otherOffset = 0;
        int numberOfClasses = 0;
        if (!translationList.isEmpty()) {
            if (index == translationList.get(translationList.size()-1)+1) {
                return new ClassInfoListIndexHolder(-1,-1,false,-1);
            }
            otherOffset = translationList.get(translationList.size()-1);
            numberOfClasses = translationList.size();
        }
        return new ClassInfoListIndexHolder(-1,index-otherOffset-2,true,index-numberOfClasses-1);
        
    }


    /**
     * remakes the entire studentlistTableModel and swithes out the old model in list and local variable
     * @param courseInfo
     * courseInfo to use to fill list
     */
    private void reMakeStudentListSelector(CourseInfo courseInfo) {
        // redo the entire table model for the StudentList because i don't know how to remove a col rom a model or get the table to not recreate it from the model and to then apint the checkboxex correct
        MyTableModel tmp = new MyTableModel();
        tmp.addColumn("Pages");
        tmp.addRow(new Object[] {"Summary"});
        //goes through all classes and add the students
        for (int classIndex = 0; classIndex < courseInfo.getClassInfoList().size(); classIndex++) {
            for (int studentIndex = 0; studentIndex < courseInfo.getClassInfoList().get(classIndex).getStudents().size(); studentIndex++) {
                // add the student to the userSelection table on the left
                tmp.addRow(new Object[]{courseInfo.getClassInfoList().get(classIndex).getStudents().get(studentIndex)});
            }
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
            MyJTable studentTable = new MyJTable(studentTableModel,0,0,new ArrayList<>(Collections.singletonList(courseInfo.getCourseGoalModel().getMilestone().size()*3 + 1)));
            studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(givenStudents.get(studentIndex), courseInfo.getCourseName());

            studentTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    Vector<Object> vector = (Vector<Object>) studentTableModel.getDataVector().get(studentTable.getSelectedRow());
                    if (studentTable.getSelectedRow() % 2 != 1) {
                        int sum = 0;
                        for (int index = 1; index < vector.size() - 1; index++) {
                            if (vector.get(index).getClass().equals(String.class)) {
                                sum += Integer.parseInt((String) vector.get(index));
                            } else if (vector.get(index).getClass().equals(Integer.class)) {
                                sum += (Integer) vector.get(index);
                            }
                        }
                        // parse the new value just typed in to just do i once
                        int newValue = 0;
                        if (vector.get(studentTable.getSelectedColumn()).getClass().equals(String.class)) {
                            newValue = Integer.parseInt((String) vector.get(studentTable.getSelectedColumn()));
                        } else if (vector.get(studentTable.getSelectedColumn()).getClass().equals(Integer.class)) {
                            newValue = (Integer) vector.get(studentTable.getSelectedColumn());
                        }

                        studentTableModel.setValueAt(sum, studentTable.getSelectedRow(), courseInfo.getCourseGoalModel().getMilestone().size() * 3 + 1);

                        // change the grade in the personLexicon
                        ClassInfoListIndexHolder indexHolder = selectListIndexTranslator(currentStudentPageIndex);
                        if (!indexHolder.otherStudentList) {
                            StudentGrade grade = personLexicon.getCourseGradeByPerson(courseInfo.getClassInfoList().get(
                                    indexHolder.listIndex).getStudents().get(indexHolder.studentIndex), courseInfo.getCourseName());
                            grade.setValueAt(studentTable.getSelectedRow() / 2, studentTable.getSelectedColumn() - 1, newValue);
                            personLexicon.insertStudentGrade(courseInfo.getClassInfoList().get(indexHolder.listIndex).getStudents().get(
                                    indexHolder.studentIndex), grade, courseInfo.getCourseName());
                        } else {
                            StudentGrade grade = personLexicon.getCourseGradeByPerson(courseInfo.getOtherEnlistedStudents().get(
                                    indexHolder.studentIndex), courseInfo.getCourseName());
                            grade.setValueAt(studentTable.getSelectedRow() / 2, studentTable.getSelectedColumn() - 1, newValue);
                            personLexicon.insertStudentGrade(courseInfo.getOtherEnlistedStudents().get(indexHolder.studentIndex),
                                                             grade, courseInfo.getCourseName());
                        }

                        contentChanged = true;
                        changePage = false;
                        setupSummaryPage();
                    }
                    else {
                        if (studentTable.getSelectedColumn() != 0 && studentTable.getSelectedColumn() != studentTable.getColumnCount()-1) {
                            int newMaxPoint = 0;
                            if (vector.get(studentTable.getSelectedColumn()).getClass().equals(String.class)) {
                                newMaxPoint = Integer.parseInt((String) vector.get(studentTable.getSelectedColumn()));
                            } else if (vector.get(studentTable.getSelectedColumn()).getClass().equals(Integer.class)) {
                                newMaxPoint = (Integer) vector.get(studentTable.getSelectedColumn());
                            }
                            if (courseInfo.getCourseGoalModel().getMaxPoits().get(studentTable.getSelectedRow() / 2).get(studentTable.getSelectedColumn() - 1) != newMaxPoint) {
                                courseInfo.getCourseGoalModel().getMaxPoits().get(studentTable.getSelectedRow() / 2).set(studentTable.getSelectedColumn() - 1, newMaxPoint);
                                resetUpdateToOther(currentStudentPageIndex);
                            }
                        }
                    }
                }

                @Override
                public void editingCanceled(ChangeEvent e) {

                }
            });
            // adds the first column
            studentTableModel.addColumn("Goals");
            // adds all the columns of partGoals to this student table
            for (int gradeLevel = 0; gradeLevel < 3; gradeLevel++) {
                for (int colIndex = 0; colIndex < courseInfo.getCourseGoalModel().getMilestone().size(); colIndex++) {
                    studentTableModel.addColumn(courseInfo.getCourseGoalModel().getMilestone().get(colIndex)+": "+gradeLevelArrayChar[gradeLevel]);
                }
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

                // add the maxPoints for each objective
                Object[] maxObjects = new Object[courseInfo.getCourseGoalModel().getMaxPoits().get(gradeRow).size()+1];
                maxObjects[0] = "Objective Max Points";
                for (int maxPointIndex = 0; maxPointIndex < courseInfo.getCourseGoalModel().getMaxPoits().get(gradeRow).size(); maxPointIndex++) {
                    maxObjects[maxPointIndex+1] = courseInfo.getCourseGoalModel().getMaxPoits().get(gradeRow).get(maxPointIndex);
                }
                studentTableModel.addRow(maxObjects);
            }

            studentGradeContainer.add(new JScrollPane(studentTable));
            studentPages.add(studentGradeContainer);
        }
    }

    /**
     *
     */
    private void setupSummaryPage() {
        if (contentChanged) {
            summaryTableContainer.removeAll();
            DefaultTableModel summaryTableModel = new DefaultTableModel();

            // makes Jtable with rows between 0 and given number un selectable
            MyJTable summaryTable = new MyJTable(summaryTableModel, 0, courseInfo.getCourseGoalModel().getMilestone().size()*3+1, new ArrayList<>());

            summaryTableModel.addColumn("Students");

            for (int goalModelRow = 0; goalModelRow < courseInfo.getCourseGoalModel().getObjective().size(); goalModelRow++) {
                for (int gradeLevel = 0; gradeLevel < 3; gradeLevel++) {
                    summaryTableModel.addColumn(courseInfo.getCourseGoalModel().getObjective().get(goalModelRow) + " " +
                                                gradeLevelArrayChar[gradeLevel]);
                }
            }


            // goes through the students grades and sums them for each goal and show them in the summaryPage
            for (int groupIndex = 0; groupIndex < courseInfo.getClassInfoList().size(); groupIndex++) {
                if (!courseInfo.getClassInfoList().get(groupIndex).getClassName().equals("No Class")) {
                    summaryTableAddStudent(summaryTableModel, courseInfo.getClassInfoList().get(groupIndex).getStudents());
                }
            }
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
        reMakeStudentListSelector(courseInfo);
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


    private class ClassInfoListIndexHolder {
        public int listIndex;
        public int studentIndex;
        public boolean otherStudentList;
        public int totalIndex;

        public ClassInfoListIndexHolder(int listIndex, int studentIndex, boolean otherStudentList, int totalIndex) {
            this.listIndex = listIndex;
            this.studentIndex = studentIndex;
            this.otherStudentList = otherStudentList;
            this.totalIndex = totalIndex;
        }
    }
}

