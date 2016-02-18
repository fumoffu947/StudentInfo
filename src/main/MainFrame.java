package main;

import main.Creation.CourseCreation.AddStudentTeacherToCourse;
import main.Creation.CourseCreation.ChooseGroupPage;
import main.Creation.CourseCreation.CreateCourse;
import main.Creation.CreateYearClass;
import main.Creation.GetListOfStudents;
import main.Creation.PersonCreation;
import main.DataStore.*;
import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.*;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;
import main.Interfaces.Panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class MainFrame extends JFrame {

    // interfaces needed to work
    private final AddToYearHolderPage addToYearHolderPage;
    private final StudentClicked studentClicked;
    private final SwitchToAddStudentTeacherToCourse switchToAddStudentTeacherToCourse;
    private RePackWindow rePackWindow;
    private YearHolderPage yearHolderPage;
    private GroupDataTransfer groupDataTransfer;
    private SwitchToGivenPanel switchToGivenPanel;
    private StartGetListOfStudents startGetListOfStudents;
    private SwitchToStudentCourseGrade switchToStudentCourseGrade = new SwitchToStudentCourseGrade() {
        @Override
        public void switchToCourseGradePage(CourseInfo courseInfo) {
            setNewPage(new StudentCourseGrade(courseInfo, mainFrame.getJMenuBar(), personLexicon, rePackWindow, startGetListOfStudents));
        }

        @Override
        public void switchToCourseGradePageAndAddCourseInfo(CourseInfo courseInfo) {
            coursesPage.addCourse(courseInfo);
            setNewPage(new StudentCourseGrade(courseInfo, mainFrame.getJMenuBar(), personLexicon, rePackWindow, startGetListOfStudents));
        }
    };

    // classes with data needed
    private PersonLexicon personLexicon = new PersonLexicon();
    private PanelHistoryStore panelHistoryStore = new PanelHistoryStore();
    private CoursesPage coursesPage;
    private JFrame mainFrame = this;

    public static void main(String[] args) {
        new MainFrame();
    }

    public RePackWindow getRePackWindow() {
        return rePackWindow;
    }

    public PersonLexicon getPersonLexicon() {
        return personLexicon;
    }

    public MainFrame() {
        super("The InfoPager");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                DataSaver dataSaver = new DataSaver(personLexicon,yearHolderPage,coursesPage);
                dataSaver.run();
            }
        });

        this.startGetListOfStudents = new StartGetListOfStudents() {
            @Override
            public void startGetStudents(StudentCourseGrade studentCourseGrade) {
                setNewPage(new GetListOfStudents(personLexicon,rePackWindow,mainFrame.getJMenuBar(),switchToGivenPanel,studentCourseGrade));
            }
        };

        this.switchToGivenPanel = new SwitchToGivenPanel() {
            @Override
            public void switchTo(Panel panel) {
                setNewPage(panel);
            }
        };

        this.groupDataTransfer = new GroupDataTransfer() {
            @Override
            public ClassInfo getClassInfoByName(String className) {
                return yearHolderPage.getClassInfoByName(className);
            }
        };

        setupContentDir();

        this.rePackWindow = () -> {
            mainFrame.setMinimumSize(mainFrame.getSize());
            mainFrame.setMaximumSize(mainFrame.getSize());
            mainFrame.pack();
            mainFrame.repaint();
            mainFrame.setMinimumSize(null);
            mainFrame.setMaximumSize(null);
        };

        this.addToYearHolderPage = new AddToYearHolderPage() {
            @Override
            public void addClass(ClassInfo classInfo) {
                yearHolderPage.addClassToPage(classInfo);
            }
        };

        this.studentClicked = new StudentClicked() {
            @Override
            public void studentData(Person person) {
                System.out.println(person);
            }
        };

        this.switchToAddStudentTeacherToCourse = new SwitchToAddStudentTeacherToCourse() {
            @Override
            public void startAddStudentTeacherToCourse(String courseName, CourseGoalModel courseGoalModel, ClassInfo classInfo) {
                setNewPage(new AddStudentTeacherToCourse(personLexicon,mainFrame.getJMenuBar(), rePackWindow,courseName, courseGoalModel,classInfo,switchToStudentCourseGrade));
            }

            @Override
            public void startChooseGroupPage(String courseName, CourseGoalModel courseGoalModel) {
                setNewPage(new ChooseGroupPage(yearHolderPage.getClasses(), switchToAddStudentTeacherToCourse, courseName,
                        courseGoalModel, mainFrame.getJMenuBar()));
            }
        };

        try {
            personLexicon = new PersonLexicon(new BufferedInputStream(new FileInputStream("content/Students.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            yearHolderPage = new YearHolderPage(new BufferedInputStream(new FileInputStream("content/Groups.txt")), personLexicon,studentClicked,rePackWindow);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            yearHolderPage = new YearHolderPage(new ArrayList<>(),studentClicked,rePackWindow);
        }

        try {
            coursesPage = new CoursesPage(new BufferedInputStream(new FileInputStream("content/Courses.txt")),personLexicon,groupDataTransfer,switchToStudentCourseGrade);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        addMenu();

        //addExampleTextFields();

        //exampleClassInfoPage();


/*
        ArrayList<String> name = new ArrayList<>();
        name.add("aaaa");
        name.add("aaaa");
        Student s = new Student(name, "aa", 0, "070", "@hotmail.com", 1);
        ArrayList<String> name2 = new ArrayList<>();
        name2.add("aaaa");
        name2.add("aaaab");
        Student s2 = new Student(name2, "aa", 0, "070", "@hotmail.com", 2);
        System.out.println(personLexicon.insertPersonToLexicon(s.getFirstName(), s));
        System.out.println(personLexicon.insertPersonToLexicon(s.getFirstName(), s));
        System.out.println(personLexicon.insertPersonToLexicon(s2.getFirstName(), s2));*/
        System.out.println(personLexicon.containsPrefix("a"));
        System.out.println(personLexicon.containsPrefix("aa"));
        System.out.println(personLexicon.containsPrefix("aaa"));
        System.out.println(personLexicon.containsPrefix("aaaa"));
        System.out.println(personLexicon.containsPrefix("aaaaa"));
        Collection<Person> collection = personLexicon.getPersonsByName("aaaa");
        Collection<Student> coll = new ArrayList<>();
        ArrayList<Integer> sg1 = new ArrayList<>();
        sg1.add(1);
        sg1.add(1);
        sg1.add(1);
        sg1.add(1);
        ArrayList<Integer> sg2 = new ArrayList<>();
        sg2.add(1);
        sg2.add(1);
        sg2.add(1);
        sg2.add(1);
        ArrayList<Integer> sg3 = new ArrayList<>();
        sg3.add(1);
        sg3.add(1);
        sg3.add(1);
        sg3.add(1);
        ArrayList<ArrayList<Integer>> grade = new ArrayList<>();
        grade.add(sg1);
        grade.add(sg2);
        grade.add(sg3);
        for (Person p : collection) {
            if (!p.isTeacher()) {
                System.out.println(p.toString());
                coll.add((Student)p);
                //System.out.println(personLexicon.insertStudentGrade((Student) p,new StudentGrade(grade),"Test"));

            }
        }

        //AddStudentTeacherToCourse addStudentTeacherToCourse = new AddStudentTeacherToCourse(personLexicon, 	getJMenuBar(),rePackWindow);
        //this.setContentPane(addStudentTeacherToCourse.getPageHolder());


        ClassInfo cI = new ClassInfo(new ArrayList<Student>(coll),"Test");
        CourseGoalModel cGM = new CourseGoalModel(new ArrayList<String>(Arrays.asList("test1","test2","test3")), new ArrayList<>(Arrays.asList("partgoal1", "partgoal2","partgoal1", "partgoal2")));
        CourseInfo c = new CourseInfo(cI,new ArrayList<Student>(),"Test",new ArrayList<Teacher>(),cGM);

        //yearHolderPage.addClassToPage(cI);
        //coursesPage.addCourse(c);

        //setContentPane(new StudentCourseGrade(c, getJMenuBar(),personLexicon, rePackWindow, startGetListOfStudents).getPageHolder());
        this.pack();
        this.setVisible(true);

        //DataSaver dataSaver = new DataSaver(personLexicon, yearHolderPage,coursesPage);
        //System.out.println("starting saving");
        //dataSaver.run();
    }

    private void setupContentDir() {
        File contentDir = new File("content");

        if (!contentDir.exists()) {
            contentDir.mkdir();
            if (contentDir.exists()) {
                System.out.println("direktory creted");
            }
        }
        if (contentDir.exists()) {
            File studentsFile = new File("content/Students.txt");
            File groupsFile = new File("content/Groups.txt");
            File coursesFile = new File("content/Courses.txt");
            try {
                if (!studentsFile.exists()) {
                    studentsFile.createNewFile();
                    System.out.println("Students.txt created");
                }
                if (!groupsFile.exists()) {
                    groupsFile.createNewFile();
                    System.out.println("Groups.txt created");
                }
                if (!coursesFile.exists()) {
                    coursesFile.createNewFile();
                    System.out.println("course.txt created");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void exampleClassInfoPage() {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        name.add("aaaa");
        name.add("aaaa");
        students.add(new Student(name, "aaab", 0, "aaaa", "aaaa", 1));
        students.add(new Student(name, "aabb", 0, "aaaa", "aaaa", 2));
        students.add(new Student(name, "abbb", 0, "aaaa", "aaaa", 3));
        students.add(new Student(name, "bbbb", 0, "aaaa", "aaaa", 4));

        ClassInfo ca1 = new ClassInfo(students, "åk1");
        ClassInfo ca2 = new ClassInfo(students, "åk2");
        ClassInfo ca3 = new ClassInfo(students, "åk3");

        ArrayList<ClassInfo> list = new ArrayList<>();
        list.add(ca1);
        list.add(ca2);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);
        list.add(ca3);


        yearHolderPage = new YearHolderPage(list, studentClicked, rePackWindow);
    }

    private void addExampleTextFields() {
        final JTextField textArea = new JTextField("hihi", 20);
        textArea.setBorder(new LineBorder(Color.BLACK));
        JTextField textArea2 = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(textArea);
        panel.add(textArea2);
        this.setContentPane(panel);

        final JTextField p = (JTextField) panel.getComponent(0);
        System.out.println(p.getText());
    }

    private void setNewPage(main.Interfaces.Panel panel) {
        Panel currentPanel = panelHistoryStore.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.clearMenuBar(mainFrame.getJMenuBar());
        }
        panelHistoryStore.addPanel(panel);
        setContentPane(panel.getPageHolder());
        rePackWindow.rePackWindow();
    }

    private void goBackOnePanel() {
        Panel currentPanel = panelHistoryStore.getCurrentPanel();
        Panel panel = panelHistoryStore.getPreviousPanel();
        if (panel != null) {
            if (currentPanel != null) {
                currentPanel.clearMenuBar(mainFrame.getJMenuBar());
            }
            panel.setupMenuBar(mainFrame.getJMenuBar());
            setContentPane(panel.getPageHolder());
            rePackWindow.rePackWindow();
        }
    }

    private void goForwardOnePanel() {
        Panel currentPanel = panelHistoryStore.getCurrentPanel();
        Panel panel = panelHistoryStore.getSubsequentPanel();
        if (panel != null) {
            if (currentPanel != null) {
                currentPanel.clearMenuBar(mainFrame.getJMenuBar());
            }
            panel.setupMenuBar(mainFrame.getJMenuBar());
            setContentPane(panel.getPageHolder());
            rePackWindow.rePackWindow();
        }
    }

    private void addMenu() {
        JMenu menu = new JMenu("ALT");
        JMenuItem exit = new JMenuItem("Exit");
        menu.add(exit);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        addCreateMenu(menuBar);

        addDataHolderPage(menuBar);

        JButton backOnePanel = new JButton();
        backOnePanel.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackOnePanel();
            }
        });
        backOnePanel.setText("Back");
        menuBar.add(backOnePanel);

        JButton forwardOnePanel = new JButton();
        forwardOnePanel.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goForwardOnePanel();
            }
        });
        forwardOnePanel.setText("Forward");
        menuBar.add(forwardOnePanel);

        this.setJMenuBar(menuBar);
    }

    private void addDataHolderPage(JMenuBar jMenuBar) {
        JMenu menu = new JMenu("Show"); // #################################### kom på något bättre

        JMenuItem showYearHolderPage = new JMenuItem();
        showYearHolderPage.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(yearHolderPage);
            }
        });
        showYearHolderPage.setText("YearClasses");
        menu.add(showYearHolderPage);

        JMenuItem showCoursePage = new JMenuItem();
        showCoursePage.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(coursesPage);
            }
        });
        showCoursePage.setText("CoursePage");
        menu.add(showCoursePage);

        jMenuBar.add(menu);
    }

    private void addCreateMenu(JMenuBar jMenuBar) {
        JMenu menu = new JMenu("Create");

        JMenuItem createCourse = new JMenuItem();
        createCourse.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(new CreateCourse(rePackWindow, mainFrame.getJMenuBar(),switchToAddStudentTeacherToCourse));
            }
        });
        createCourse.setText("CreateCourse");
        menu.add(createCourse);

        JMenuItem createYearClass = new JMenuItem();
        createYearClass.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateYearClass createYearClass = new CreateYearClass(personLexicon, rePackWindow, mainFrame.getJMenuBar(), addToYearHolderPage);
                setNewPage(createYearClass);
            }
        });
        createYearClass.setText("CreateYearClass");
        menu.add(createYearClass);

        JMenuItem studentCreation = new JMenuItem();
        studentCreation.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PersonCreation sc = new PersonCreation(student -> System.out.println(student.getFirstName()), 0, personLexicon);
                setNewPage(sc);
            }
        });
        studentCreation.setText("Create New Student");
        menu.add(studentCreation);

        jMenuBar.add(menu);
    }
}
