package main.DataStore;

import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.InterfaceDataTransfer.GroupDataTransfer;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;
import main.Interfaces.Person;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    private JTable coursesInfoTable = new JTable(coursesAndInfoTableModel);

    public CoursesPage(List<CourseInfo> courses, SwitchToStudentCourseGrade switchToStudentCourseGrade) {
        this.courses = courses;
        setupTableInfo(courses, switchToStudentCourseGrade);
    }

    public CoursesPage(BufferedInputStream fileReader, PersonLexicon personLexicon, GroupDataTransfer groupDataTransfer, SwitchToStudentCourseGrade switchToStudentCourseGrade) {
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


        setupTableInfo(courses, switchToStudentCourseGrade);
    }

    private void loadCourseFromString(String[] courseInfo, PersonLexicon personLexicon, GroupDataTransfer groupDataTransfer) {
        String courseName = courseInfo[0];
        ClassInfo classInfo = groupDataTransfer.getClassInfoByName(courseInfo[1]);
        String[] otherEnlisted = courseInfo[2].split(",");
        ArrayList<Student> otherEnlistedArrayList = new ArrayList<>();

        String[] teachers = courseInfo[3].split(",");
        ArrayList<Teacher> teacherArrayList = new ArrayList<>();

        String[] gradeModelArray = courseInfo[4].split(":");
        List<String> goals = Arrays.asList(gradeModelArray[0].split(","));
        List<String> parGoals = Arrays.asList(gradeModelArray[1].split(","));

        // find and add teachers to an ArrayList
        for (int teacherIndex = 0; teacherIndex < teachers.length; teacherIndex++) {
            String[] personInfo = teachers[teacherIndex].split(" ");
            Collection<Person> foundPersons = personLexicon.getPersonsByName(personInfo[0]);
            for (Person person : foundPersons) {
                if (person.getID() == Integer.parseInt(personInfo[1]) && person.isTeacher()) {
                    teacherArrayList.add((Teacher) person);
                }
            }
        }

        // find and add other enlisted student to an ArrayList
        for (int otherIndex = 0; otherIndex < otherEnlisted.length; otherIndex++) {
            String[] personInfo = otherEnlisted[otherIndex].split(" ");
            Collection<Person> foundPersons = personLexicon.getPersonsByName(personInfo[0]);
            for (Person person : foundPersons) {
                if (person.getID() == Integer.parseInt(personInfo[1]) && !person.isTeacher()) {
                    otherEnlistedArrayList.add((Student) person);
                }
            }
        }

        courses.add(new CourseInfo(classInfo,otherEnlistedArrayList,courseName,teacherArrayList,new CourseGoalModel(goals,parGoals)));
    }

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
            if (courseInfo.getClassInfo() != null) {
                size = courseInfo.getClassInfo().getStudents().size();
                className = courseInfo.getClassInfo().getClassName();
            }

            coursesAndInfoTableModel.addRow(new Object[] {courseInfo.getCourseName(),courseInfo.getTeachers().size(),
                                                            className, size,
                                                            courseInfo.getOtherEnlistedStudents().size()});
        }

        // set so that the user only can select one course and so that that course will be show with the info
        coursesInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesInfoTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (e.getValueIsAdjusting() && lsm.isSelectedIndex(i)) {
                        switchToStudentCourseGrade.switchToCourseGradePage(courses.get(i));
                    }
                }
                lsm.clearSelection();
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

    public List<CourseInfo> getCourses() {
        return courses;
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {

    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {

    }

    public JPanel getPageHolder() {
        return pageHolder;
    }
}
