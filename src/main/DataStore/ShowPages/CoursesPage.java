package main.DataStore.ShowPages;

import main.DataStore.*;
import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.InterfaceDataTransfer.GroupDataTransfer;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;
import main.Interfaces.Person;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by phili on 2016-01-16.
 * this is the page where all the courses can be found
 */
public class CoursesPage implements main.Interfaces.Panel {

    private List<CourseInfo> courses;

    private JPanel pageHolder = new JPanel(new GridBagLayout());

    private DefaultTableModel coursesAndInfoTableModel = new DefaultTableModel();
    private PrivateJTable coursesInfoTable = new PrivateJTable(coursesAndInfoTableModel);

    private JButton removedSelectedCourse;
    private JButton goToCourse;

    public CoursesPage(List<CourseInfo> courses, SwitchToStudentCourseGrade switchToStudentCourseGrade) {
        this.courses = courses;
        setupTableInfo(courses, switchToStudentCourseGrade);
    }

    /**
     * goes through the fileReader and uses leadCourseFromString to create a course when the right end character appears
     * @param fileReader
     * reader to read string from
     * @param personLexicon
     * the lexicon to load persons from
     * @param groupDataTransfer
     * the interface to load groups from
     * @param switchToStudentCourseGrade
     * the interface to switch to chosen course to
     */
    public CoursesPage(BufferedInputStream fileReader, PersonLexicon personLexicon, GroupDataTransfer groupDataTransfer,
                       SwitchToStudentCourseGrade switchToStudentCourseGrade, JMenuBar jMenuBar) {
        courses = new ArrayList<>();

        try {
            int character = fileReader.read();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while(character != -1) {
                if ('<' == character) {
                    if (byteArrayOutputStream.size() != 0) {
                        String personString = new String(Base64.getDecoder().decode(byteArrayOutputStream.toByteArray()));
                        loadCourseFromString(personString.split(";"), personLexicon, groupDataTransfer);
                        byteArrayOutputStream.reset();
                    }
                }else {
                    byteArrayOutputStream.write(character);
                }

                character = fileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.removedSelectedCourse = new JButton();
        removedSelectedCourse.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null,"Are you sure you want to remove this course: "+courses.get(coursesInfoTable.getSelectedRow()),"Course Removal",JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    for (int courseindex = 0; courseindex < courses.get(coursesInfoTable.getSelectedRow()).getClassInfoList().size(); courseindex++) {
                        personLexicon.removeCourseFromPresons(courses.get(coursesInfoTable.getSelectedRow()).getClassInfoList().get(courseindex).getStudents(),courses.get(coursesInfoTable.getSelectedRow()).getCourseName());
                    }
                    personLexicon.removeCourseFromPresons(courses.get(coursesInfoTable.getSelectedRow()).getOtherEnlistedStudents(), courses.get(coursesInfoTable.getSelectedRow()).getCourseName());
                    courses.remove(coursesInfoTable.getSelectedRow());
                    coursesAndInfoTableModel.removeRow(coursesInfoTable.getSelectedRow());
                }
            }
        });
        this.removedSelectedCourse.setText("Remove Selected Course");

        this.goToCourse = new JButton();
        this.goToCourse.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToStudentCourseGrade.switchToCourseGradePage(courses.get(coursesInfoTable.getSelectedRow()));
            }
        });
        this.goToCourse.setText("Go To Selected Course");

        setupTableInfo(courses, switchToStudentCourseGrade);
    }

    /**
     * loads a course from the given string with given personLexicon and given GroupTransfer interface
     * @param courseInfo
     * the string that will be used to create the course
     * @param personLexicon
     * lexicon used to acquire the students and teachers
     * @param groupDataTransfer
     * the interface used to acquire the groups of students in the course
     */
    private void loadCourseFromString(String[] courseInfo, PersonLexicon personLexicon, GroupDataTransfer groupDataTransfer) {
        String courseName = courseInfo[0];
        String[] classInfoNames = courseInfo[1].split(",");
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        String[] otherEnlisted = courseInfo[2].split(",");
        ArrayList<Student> otherEnlistedArrayList = new ArrayList<>();

        String[] teachers = courseInfo[3].split(",");
        ArrayList<Teacher> teacherArrayList = new ArrayList<>();

        String[] gradeModelArray = courseInfo[4].split(":");
        List<String> goals = Arrays.asList(gradeModelArray[0].split(","));
        List<String> parGoals = Arrays.asList(gradeModelArray[1].split(","));

        String[] removedStudentsID = courseInfo[5].split(",");
        List<Integer> listOfRemovedStudents = new ArrayList<>();

        String[] gradeLevelModelArray = courseInfo[6].split(":");
        List<List<Integer>> gradeLevelModel = new ArrayList<>();

        String[] maxPointsArray = gradeModelArray[2].split(",");
        List<List<Integer>> maxPointModel = new ArrayList<>();

        for (int classIndex = 0; classIndex < classInfoNames.length; classIndex++) {
            classInfos.add(groupDataTransfer.getClassInfoByName(classInfoNames[classIndex]));

        }


        for (int maxPointRowIndex = 0; maxPointRowIndex < goals.size(); maxPointRowIndex++) {
            maxPointModel.add(new ArrayList<>());
            for (int maxPointColIndex = 0; maxPointColIndex < parGoals.size() * 3; maxPointColIndex++) {
                        maxPointModel.get(maxPointRowIndex).add(Integer.parseInt(maxPointsArray[maxPointRowIndex*goals.size()+maxPointColIndex]));
            }
        }

        // find and add teachers to an ArrayList
        for (int teacherIndex = 0; teacherIndex < teachers.length; teacherIndex++) {
            if (!teachers[teacherIndex].isEmpty()) {
                String[] personInfo = teachers[teacherIndex].split(" ");
                Collection<Person> foundPersons = personLexicon.getPersonsByName(personInfo[0]);
                for (Person person : foundPersons) {
                    if (person.getID() == Integer.parseInt(personInfo[1]) && person.isTeacher()) {
                        teacherArrayList.add((Teacher) person);
                    }
                }
            }
        }

        // find and add other enlisted student to an ArrayList
        for (int otherIndex = 0; otherIndex < otherEnlisted.length; otherIndex++) {
            if (!otherEnlisted[otherIndex].isEmpty()) {
                String[] personInfo = otherEnlisted[otherIndex].split(" ");
                Collection<Person> foundPersons = personLexicon.getPersonsByName(personInfo[0]);
                for (Person person : foundPersons) {
                    if (person.getID() == Integer.parseInt(personInfo[1]) && !person.isTeacher()) {
                        otherEnlistedArrayList.add((Student) person);
                    }
                }
            }
        }

        // parse and add all the ID's of removed students
        for (int removedIndex = 0; removedIndex < removedStudentsID.length; removedIndex++) {
            listOfRemovedStudents.add(Integer.parseInt(removedStudentsID[removedIndex]));
        }

        // parse the string GradeLevelModelArray into an List<List<Integer>> array which will be made into an CourseGradeModel
        for (int gradeRow = 0; gradeRow < gradeLevelModelArray.length; gradeRow++) {
            gradeLevelModel.add(new ArrayList<>());
            String[] gradeLevelRowArray = gradeLevelModelArray[gradeRow].split(",");
            for (int gradeCol = 0; gradeCol < gradeLevelRowArray.length; gradeCol++) {
                gradeLevelModel.get(gradeRow).add(Integer.parseInt(gradeLevelRowArray[gradeCol]));
            }
        }

        courses.add(new CourseInfo(classInfos,otherEnlistedArrayList,courseName,teacherArrayList,new CourseGoalModel(goals,parGoals,maxPointModel),listOfRemovedStudents,new CourseGradeModel(gradeLevelModel)));
    }

    /**
     * sets up the table info to show all the courses
     * @param courses
     * tha list of courses to sow
     * @param switchToStudentCourseGrade
     * used to switch to a course if it is pressed
     */
    private void setupTableInfo(final List<CourseInfo> courses, final SwitchToStudentCourseGrade switchToStudentCourseGrade) {
        // add the columns
        coursesAndInfoTableModel.addColumn("Course Name");
        coursesAndInfoTableModel.addColumn("Number Of Teachers");
        coursesAndInfoTableModel.addColumn("Class name");
        coursesAndInfoTableModel.addColumn("Number Of Students From Class");
        coursesAndInfoTableModel.addColumn("Other Enlisted Students");


        //add all the info about each course to a row in the right ordered
        for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++) {
            CourseInfo courseInfo = courses.get(courseIndex);
            int size = 0;
            String className = "";
            if (courseInfo.getClassInfoList() != null) {
                for (int i = 0; i < courseInfo.getClassInfoList().size(); i++) {
                    size += courseInfo.getClassInfoList().get(i).getStudents().size();
                    className += " " + courseInfo.getClassInfoList().get(i).getClassName();
                }
            }

            coursesAndInfoTableModel.addRow(new Object[] {courseInfo.getCourseName(),courseInfo.getTeachers().size(),
                                                            className, size,
                                                            courseInfo.getOtherEnlistedStudents().size()});
        }

        // set so that the user only can select one course and so that that course will be show with the info
        coursesInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        coursesInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * adds a course to the list of courses and displays it in the Jtable
     * @param courseInfo
     * the course to be saved and displayed
     */
    public void addCourse(CourseInfo courseInfo) {
        int size = 0;
        String className = "";
        if (courseInfo.getClassInfoList() != null) {
            for (int i = 0; i < courseInfo.getClassInfoList().size(); i++) {
                size += courseInfo.getClassInfoList().get(i).getStudents().size();
                className += " " + courseInfo.getClassInfoList().get(i).getClassName();
            }
        }
        coursesAndInfoTableModel.addRow(new Object[] {courseInfo.getCourseName(),courseInfo.getTeachers().size(),
                className, size,
                courseInfo.getOtherEnlistedStudents().size()});
        courses.add(courseInfo);
    }

    /**
     * used to get the know courses
     * @return
     * list of courses
     */
    public List<CourseInfo> getCourses() {
        return courses;
    }

    /**
     * returns the course by the given name of null if no found
     * @param courseName
     * given name to match to a courseInfo
     * @return
     * returns courseInfo or null
     */
    public CourseInfo getCourseByName(String courseName) {
        for(CourseInfo courseInfo : courses) {
            if (courseInfo.getCourseName().equals(courseName)) {
                return courseInfo;
            }
        }
        return null;
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {
        jMenuBar.remove(removedSelectedCourse);
        jMenuBar.remove(goToCourse);
    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {
        jMenuBar.add(removedSelectedCourse);
        jMenuBar.add(goToCourse);
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }

    private class PrivateJTable extends JTable {
        public PrivateJTable(TableModel dm) {
            super(dm);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
