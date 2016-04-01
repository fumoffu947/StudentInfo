package main.DataStore.ShowPages;

import main.Interfaces.Panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by phili on 2016-02-18.
 * The front page that gives instructions to how the user operates the program and info about it
 */
public class FrontPage implements Panel {

    private JPanel pageHolder = new JPanel(new BorderLayout());

    private String newLine = System.getProperty("line.separator");

    private String courseCreationString = "To get to the course creation page you first heve to click on the \"Create\" button in the top left corner." +
            "Then click on \"Create Course\" to get into the first page with information to fill in. After that you get to a page with (in the top) the " +
            "name of the course, to the left an table (called Course-Goal-Model which will be explained shortly) and to the right another table called the " +newLine+
            "Grade Level Model." +newLine+
            "The so called Course-Goal-Model have three fields. The leftmost column where the objectives is written into separate rows. The uppermost row " +
            "where all the milestones are written into separate column (only one of each milestone is necessary to write where the others will automatically" +
            "be filled in). The field in between these two fields which is the max-point field where all the max-points will be written into the table for each" +
            " separate milestone." +newLine+
            "The Grade-Level-Model" +newLine+
            "After this information has been filled in the \"Continue\" button can be pressed to continue to the nex step in the process " +
            "The next step in the process is the group selection. Here the box in the \"Chosen Classes\" column are pressed to mark that group for use in this " +
            "course thus adding the students in that group to the current course. " +
            "When all groups are marked that is wanted the \"Continue\" button can be pressed to to the next step. " +
            "The next step in the process is the choosing of other students that ether don't belong to a group or single students form a group i wanted in the course. " +
            "There is also the possibility to add zero or more teachers to the course. " +
            "Students to be added can be searched after in the field in the top of the window, and if any students with the name was found they will be displayed in the" +
            " upper rightmost table. In this table the students wanted for this course can be marked by clicking the check-box and then added by clicking the button " +
            "\"Add Students\". If one or more students can be removed by marking the student in the upper leftmost table and pressing the button \"Remove Students\" in" +
            " the same manner as when adding students. " +
            "Adding teachers is done in the same manner as adding and removing students but searching for teachers is done in the field int the lower part of the window " +
            "and adding and removing by pressing the respective buttons. " +
            "When all students and teachers are added the button \"Add to course\" can be pressed to create the course with the given information from before the current info. ";
    private String groupCreationString = "To get to the group-creation page you first have to click on the \"Create\" button in the top left corner. " +
            "Then click on \"Create Group\" to get to the creation of groups. At the first page there are two tables, one search field(at the top of the window) and" +
            " one field just below the search field for the name of the group. To search for students to add to the group the search field has to be filled in with the" +
            " first name of the student that wanted. When the name has been filled in the students with the same first name will be shown in the table to the right. " +
            "Then mark the students that are wanted and then press the \"Add Students\" to add them to the group. When a name has been given to the course and at least " +
            "one student, the group can be created by pressing the \"Create Group\" button in the top of the window. ";
    private String studentTeacherCreationString = "To get to the student- and teacher-creation page you first have to click on the \"Create\" button in the top left corner. " +
            "Then click on \"Create New Person\" to get to the page to fill in info about the person. " +
            "Then it is just to fill in the four field with information (only the \"Name\" and \"Surname\" fields are necessary to create a person). " +
            "The \"Name\" field needs at least one name with at least on character and if there are more than one name just fill in all the name separated with a space. " +
            "When all information is filled in, either check the box \"Is teacher?\" if it is a teacher and, then click the \"Add Person\" button to add the person to the system.";
    private String coursePageString = "To get to the Course-page you first have to click on the \"Show\" button in the top left corner. " +
            "Then on \"Course-Page\" to get to the page that displays all the current courses in the system. For each course in tha table the number of teachers, students in " +
            " a group and other enlisted students are shown. The name of the course and names of the groups are also displayed in respective columns. " +
            "To remove a course just click on the course you want to remove adn then click the \"Remove Selected Course\" button in the top of the window. " +
            "To go to a course-page just click on the course you want to se and then click the \"Go To Selected Course\" button in the top of the window.";
    private String studentGradePageString = "To get to a specific course-page where grades can be changed for the students you first have to go to the \"Course-Page\" and " +
            "select an course and press the \"Go To Selected Course\" button. " +
            "In this page there are two tables. The table to the left is the list of all students in the course and an summary of all students(at the top of the list. " +
            "If more specific information about a students is wanted then just click on that student and the table to the right will show the points for all the milestones " +
            "for each objective with the max-points under each point with a summary after each \"E\", \"C\" and \"A\" -grade of the points before.";
    private String groupPageString = "To get to the group-page you first have to click on the \"Show\" button in the top left corner." +
            " Then click on the \"Groups\" button to show all groups with students in them. To remove a group just click on  the \"Remove Group\" button below that group.";
    private String studentsPageString = "To get to the students-page you first have to click on the \"Show\" button in the top left corner." +
            " Then click on the \"Show Students\" button to se all students currently in the system. To se information about a specific student just pres that student and " +
            "information about that student will be show in the right part of the window.";

    public FrontPage() {
        JTextField courseCreationHeader = new JTextField("Course Creation");
        courseCreationHeader.setEditable(false);
        JTextField groupCreationHeader = new JTextField("Group Creation");
        groupCreationHeader.setEditable(false);
        JTextField studentTeacherCreationHeader = new JTextField("Person Creation");
        studentTeacherCreationHeader.setEditable(false);
        JTextField coursePageHeader = new JTextField("Course-Page");
        coursePageHeader.setEditable(false);
        JTextField studentGradePageHeader = new JTextField("Grade-Page");
        studentGradePageHeader.setEditable(false);
        JTextField groupPageHeader = new JTextField("Groups");
        groupPageHeader.setEditable(false);
        JTextField studentsPageHeader = new JTextField("All Students");
        studentsPageHeader.setEditable(false);

        JTextArea courseCreationInfoArea = new JTextArea();
        courseCreationInfoArea.setLineWrap(true);
        courseCreationInfoArea.setWrapStyleWord(true);
        courseCreationInfoArea.setEditable(false);
        courseCreationInfoArea.setText(courseCreationString);

        JTextArea groupCreationArea = new JTextArea();
        groupCreationArea.setWrapStyleWord(true);
        groupCreationArea.setLineWrap(true);
        groupCreationArea.setEditable(false);
        groupCreationArea.setText(groupCreationString);

        JTextArea studentTeacherCreationArea = new JTextArea();
        studentTeacherCreationArea.setWrapStyleWord(true);
        studentTeacherCreationArea.setLineWrap(true);
        studentTeacherCreationArea.setEditable(false);
        studentTeacherCreationArea.setText(studentTeacherCreationString);

        JTextArea coursePageArea = new JTextArea();
        coursePageArea.setWrapStyleWord(true);
        coursePageArea.setLineWrap(true);
        coursePageArea.setEditable(false);
        coursePageArea.setText(coursePageString);

        JTextArea studentGradePageArea = new JTextArea();
        studentGradePageArea.setWrapStyleWord(true);
        studentGradePageArea.setLineWrap(true);
        studentGradePageArea.setEditable(false);
        studentGradePageArea.setText(studentGradePageString);

        JTextArea groupPageArea = new JTextArea();
        groupPageArea.setWrapStyleWord(true);
        groupPageArea.setLineWrap(true);
        groupPageArea.setEditable(false);
        groupPageArea.setText(groupPageString);

        JTextArea studentsPageArea = new JTextArea();
        studentsPageArea.setWrapStyleWord(true);
        studentsPageArea.setLineWrap(true);
        studentsPageArea.setEditable(false);
        studentsPageArea.setText(studentsPageString);

        JPanel courseCreationInfoContainer = new JPanel(new GridBagLayout());
        JPanel groupCreationContainer = new JPanel(new GridBagLayout());
        JPanel studentTeacherCreationContainer = new JPanel(new GridBagLayout());
        JPanel coursePageContainer = new JPanel(new GridBagLayout());
        JPanel studentGradePageContainer = new JPanel(new GridBagLayout());
        JPanel groupPageContainer = new JPanel(new GridBagLayout());
        JPanel studentsPageContainer = new JPanel(new GridBagLayout());

        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.gridy = 0;
        headerConstraints.weightx = 1.0;
        headerConstraints.weighty = 1.0;
        headerConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridy = 1;
        textConstraints.weightx = 1.0;
        textConstraints.weighty = 1.0;
        textConstraints.fill = GridBagConstraints.BOTH;

        courseCreationInfoContainer.add(courseCreationHeader, headerConstraints);
        groupCreationContainer.add(groupCreationHeader, headerConstraints);
        studentTeacherCreationContainer.add(studentTeacherCreationHeader, headerConstraints);
        coursePageContainer.add(coursePageHeader, headerConstraints);
        studentGradePageContainer.add(studentGradePageHeader, headerConstraints);
        groupPageContainer.add(groupPageHeader, headerConstraints);
        studentsPageContainer.add(studentsPageHeader, headerConstraints);

        courseCreationInfoContainer.add(courseCreationInfoArea,textConstraints);
        groupCreationContainer.add(groupCreationArea,textConstraints);
        studentTeacherCreationContainer.add(studentTeacherCreationArea,textConstraints);
        coursePageContainer.add(coursePageArea,textConstraints);
        studentGradePageContainer.add(studentGradePageArea,textConstraints);
        groupPageContainer.add(groupPageArea,textConstraints);
        studentsPageContainer.add(studentsPageArea,textConstraints);

        GridBagConstraints generalConstrains = new GridBagConstraints();
        generalConstrains.gridy = 0;
        generalConstrains.weightx = 1.0;
        generalConstrains.weighty = 1.0;
        generalConstrains.fill = GridBagConstraints.BOTH;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        container.add(courseCreationInfoContainer);
        container.add(groupCreationContainer);
        container.add(studentTeacherCreationContainer);
        container.add(coursePageContainer);
        container.add(studentGradePageContainer);
        container.add(groupPageContainer);
        container.add(studentsPageContainer);
        pageHolder.add(new JScrollPane(container));
    }

    @Override public void clearMenuBar(final JMenuBar jMenuBar) {

    }

    @Override public void setupMenuBar(final JMenuBar jMenuBar) {

    }

    @Override public JPanel getPageHolder() {
	return pageHolder;
    }
}
