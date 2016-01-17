package main.DataStore;

import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.RePackWindow;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by phili on 2016-01-16.
 */
public class StudentCourseGrade {

    private final PersonLexicon personLexicon;
    private CourseInfo courseInfo;
    private int currentStudentPageIndex = 0;

    private List<JPanel> studentPages = new ArrayList<>();

    private GridBagLayout gridBagLayout = new GridBagLayout();
    private JPanel pageHolder = new JPanel(gridBagLayout);
    //private GridBagLayout gbl = new GridBagLayout();
    private JPanel studentGradePage = new JPanel(new BorderLayout());

    private DefaultTableModel studentListTableModel = new DefaultTableModel();

    private MyJTable studentList = new MyJTable(studentListTableModel,0,0, new ArrayList<>());

    public StudentCourseGrade(CourseInfo courseInfo, PersonLexicon personLexicon, RePackWindow rePackWindow) {
        this.courseInfo = courseInfo;
        this.personLexicon = personLexicon;


        GridBagConstraints studentListConstraints = new GridBagConstraints();
        studentListConstraints.gridx = 0;
        studentListConstraints.weightx = 0.5;
        studentListConstraints.weighty = 1.0;
        studentListConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(new JScrollPane(studentList) , studentListConstraints);

        GridBagConstraints studentPageGradeConstraints = new GridBagConstraints();
        studentPageGradeConstraints.gridx = 1;
        studentPageGradeConstraints.weightx = 3.0;
        studentPageGradeConstraints.weighty = 1.0;
        studentPageGradeConstraints.fill = GridBagConstraints.BOTH;
        pageHolder.add(studentGradePage, studentPageGradeConstraints);

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
                        rePackWindow.rePackWindow();
                    }
                }
            }
        });

        studentListTableModel.addRow(new Object[] {"Summary"});

        // adds the summary page to the studentPage
        setupSummaryPage(courseInfo, personLexicon);

        // goes through the students and add separate pages for each one of them
        for (int studentIndex = 0; studentIndex < courseInfo.getClassInfo().getStudents().size(); studentIndex++) {
            // add the student to the userselection table on the left
            studentListTableModel.addRow(new Object[] {courseInfo.getClassInfo().getStudents().get(studentIndex)});

            JPanel studentGradeContainer = new JPanel(new BorderLayout());
            DefaultTableModel studentTableModel = new DefaultTableModel();
            MyJTable studentTable = new MyJTable(studentTableModel,0,0,new ArrayList<>(Collections.singletonList(courseInfo.getCourseGoalModel().getPartGoals().size() + 1)));
            studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(courseInfo.getClassInfo().getStudents().get(studentIndex), courseInfo.getCourseName());

            studentTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    DefaultCellEditor a = (DefaultCellEditor) e.getSource();
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
                    studentTableModel.setValueAt(sum,studentTable.getSelectedRow(),courseInfo.getCourseGoalModel().getPartGoals().size() + 1);
                }

                @Override
                public void editingCanceled(ChangeEvent e) {

                }
            });
            // adds the first column
            studentTableModel.addColumn("Goals");
            // adds all the columns of partGoals to this student table
            for (int colIndex = 0; colIndex < courseInfo.getCourseGoalModel().getPartGoals().size(); colIndex++) {
                studentTableModel.addColumn(courseInfo.getCourseGoalModel().getPartGoals().get(colIndex));
            }
            studentTableModel.addColumn("Summary");
            //adds all the rows of data to this table, which is gotten from this students StudentGrade from personLexicon
            // and adds all of then to an ObjectArray which then is the full data row
            for (int gradeRow = 0; gradeRow < courseInfo.getCourseGoalModel().getGoals().size(); gradeRow++) {
                Object[] grade = studentGrade.getGrades().get(gradeRow).toArray();
                int goalSum = studentGrade.getGrades().get(gradeRow).stream().mapToInt(Integer::intValue).sum();
                Object[] rowData = new Object[grade.length+2];
                rowData[grade.length+1] = goalSum;
                rowData[0] = courseInfo.getCourseGoalModel().getGoals().get(gradeRow);
                // adds the scores from the grade to the current row ObjectArray
                for (int index = 1; index < grade.length+1; index++) {
                    rowData[index] = grade[index-1];
                }
                studentTableModel.addRow(rowData);

                studentGradeContainer.add(new JScrollPane(studentTable));
                studentPages.add(studentGradeContainer);
            }
        }

        // goes through the extraStudents and add separate pages for each one of them
        for (int extraStudent = 0; extraStudent < courseInfo.getOtherEnlistedStudents().size(); extraStudent++) {
            // add the student to the userSelection table on the left
            studentListTableModel.addRow(new Object[] {courseInfo.getOtherEnlistedStudents().get(extraStudent)});

            JPanel studentGradeContainer = new JPanel(new BorderLayout());
            DefaultTableModel studentTableModel = new DefaultTableModel();
            MyJTable studentTable = new MyJTable(studentTableModel,0,0,new ArrayList<>(Collections.singletonList(courseInfo.getCourseGoalModel().getPartGoals().size() + 1)));
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(courseInfo.getOtherEnlistedStudents().get(extraStudent), courseInfo.getCourseName());
            // adds the first column
            studentTableModel.addColumn("Goals");
            // adds all the columns of partGoals to this student table
            for (int colIndex = 0; colIndex < courseInfo.getCourseGoalModel().getPartGoals().size(); colIndex++) {
                studentTableModel.addColumn(courseInfo.getCourseGoalModel().getPartGoals().get(colIndex));
            }
            studentTableModel.addColumn("Summary");
            // adds all the rows of data to this table, which is gotten from this students StudentGrade from personLexicon
            // and adds all of then to an ObjectArray which then is the full data row
            for (int gradeRow = 0; gradeRow < courseInfo.getCourseGoalModel().getGoals().size(); gradeRow++) {
                Object[] grade = studentGrade.getGrades().get(gradeRow).toArray();
                int goalSum = studentGrade.getGrades().get(gradeRow).stream().mapToInt(Integer::intValue).sum();
                Object[] rowData = new Object[grade.length+2];
                rowData[grade.length+1] = goalSum;
                // adds the scores from the grade to the current row ObjectArray
                rowData[0] = courseInfo.getCourseGoalModel().getGoals().get(gradeRow);
                for (int index = 1; index < grade.length+1; index++) {
                    rowData[index] = grade[index-1];
                }
                studentTableModel.addRow(rowData);

                studentGradeContainer.add(new JScrollPane(studentTable));
                studentPages.add(studentGradeContainer);
            }
        }


    }

    private void setupSummaryPage(CourseInfo courseInfo, PersonLexicon personLexicon) {
        JPanel summaryTableContainer = new JPanel(new BorderLayout());
        DefaultTableModel summaryTableModel = new DefaultTableModel();
        MyJTable summaryTable = new MyJTable(summaryTableModel,0,courseInfo.getClassInfo().getStudents().size()+courseInfo.getOtherEnlistedStudents().size(), new ArrayList<>());

        summaryTableModel.addColumn("Goals", courseInfo.getCourseGoalModel().getGoals().toArray());

        // goes through the students grades and sums them for each goal and show them in the summaryPage
        for (int studentIndex = 0; studentIndex < courseInfo.getClassInfo().getStudents().size(); studentIndex++) {
            ArrayList<Integer> columnData = new ArrayList<>();
            StudentGrade studentGrade = personLexicon.getCourseGradeByPerson(courseInfo.getClassInfo().getStudents().get(studentIndex), courseInfo.getCourseName());
            //sums each grade and adds to an array to be the columnDataObject
            for (int gradeRow = 0; gradeRow < studentGrade.getGrades().size(); gradeRow++) {
                columnData.add(studentGrade.getGrades().get(gradeRow).stream().mapToInt(Integer::intValue).sum());
            }
            summaryTableModel.addColumn(courseInfo.getClassInfo().getStudents().get(studentIndex), columnData.toArray());
        }
        summaryTableContainer.add(new JScrollPane(summaryTable));
        studentPages.add(summaryTableContainer);
        studentGradePage.add(summaryTableContainer);
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }
}

class MyJTable extends JTable {

    private int columnStart;
    private int columnEnd;
    private ArrayList<Integer> singleRowArray;

    public MyJTable(TableModel dm,int columnStart, int columnEnd,ArrayList<Integer> singleRowArray) {
        super(dm);
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.singleRowArray = singleRowArray;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column >= columnStart && column <= columnEnd || singleRowArray.contains(column)) {
            return false;
        }
        return super.isCellEditable(row, column);
    }
}