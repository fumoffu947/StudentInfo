package main;

import main.Creation.CourseCreation.AddStudentTeacherToCourse;
import main.Creation.CourseCreation.ChooseGroupPage;
import main.Creation.CourseCreation.CreateCourse;
import main.Creation.CourseCreation.CreateFromCourse;
import main.Creation.CreateYearClass;
import main.Creation.GetListOfStudents;
import main.Creation.PersonCreation;
import main.DataStore.*;
import main.DataStore.CoursePakage.CourseGoalModel;
import main.DataStore.CoursePakage.CourseGradeModel;
import main.DataStore.CoursePakage.CourseInfo;
import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.ShowPages.*;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.*;
import main.Interfaces.PaneInterfaceSwitches.GroupSwitch;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.PaneInterfaceSwitches.SwitchToCreateFromCourse;
import main.Interfaces.PaneInterfaceSwitches.SwitchToStudentCourseGrade;
import main.Interfaces.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class MainFrame extends JFrame {

    // interfaces needed to work
    public static Logger logger;


    private final AddToYearHolderPage addToYearHolderPage;
    private final StudentClicked studentClicked;
    private final SwitchToAddStudentTeacherToCourse switchToAddStudentTeacherToCourse;
    private RePackWindow rePackWindow;
    private YearHolderPage yearHolderPage;
    private GroupDataTransfer groupDataTransfer;
    private SwitchToGivenPanel switchToGivenPanel;
    private StartGetListOfStudents startGetListOfStudents;
    private CourseInfoTransfer courseInfoTransfer;
    private GroupSwitch groupSwitch;
    private SwitchToCreateFromCourse switchToCreateFromCourse;
    private SwitchToStudentCourseGrade switchToStudentCourseGrade = new SwitchToStudentCourseGrade() {
        @Override
        public void switchToCourseGradePage(CourseInfo courseInfo) {
            setNewPage(new StudentCourseGrade(courseInfo, personLexicon, rePackWindow, startGetListOfStudents));
        }

        @Override
        public void switchToCourseGradePageAndAddCourseInfo(CourseInfo courseInfo) {
            coursesPage.addCourse(courseInfo);
            setNewPage(new StudentCourseGrade(courseInfo, personLexicon, rePackWindow, startGetListOfStudents));
        }
    };

    // classes with data needed
    private PersonLexicon personLexicon = new PersonLexicon();
    private PanelHistoryStore panelHistoryStore = new PanelHistoryStore();
    private CoursesPage coursesPage;
    private SettingsLoader settingsLoader;
    private FrontPage frontPage = new FrontPage();
    private JFrame mainFrame = this;

    public static void main(String[] args) {
        setupContentDir();
        try {
            File errorLog = new File("content/ErrorLog.txt");
            if (!errorLog.exists()) {
                errorLog.createNewFile();
                System.out.println("ErrorLog.txt created");
            }
            FileOutputStream errorOutStream = new FileOutputStream(errorLog);
            PrintStream errorPrintStream = new PrintStream(errorOutStream);
            System.setErr(errorPrintStream);

            File loggerFile = new File("content/LoggerFile.txt");
            if (!loggerFile.exists()) {
                loggerFile.createNewFile();
            }

            java.util.logging.Handler handler = new FileHandler("content/LoggerFile.txt");
            handler.setLevel(Level.ALL);
            handler.setFormatter(new SimpleFormatter());
            logger = Logger.getGlobal();
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);

            logger.log(Level.INFO, "Starting the mainframe, starting upp the system.");
            new MainFrame();
        }catch (IOException e) {
            e.printStackTrace();
        }
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
                DataSaver dataSaver = new DataSaver(personLexicon,yearHolderPage,coursesPage, settingsLoader, logger);
                dataSaver.run();
            }
        });

        this.startGetListOfStudents = new StartGetListOfStudents() {
            @Override
            public void startGetStudents(StudentCourseGrade studentCourseGrade) {
                setNewPage(new GetListOfStudents(personLexicon,rePackWindow,mainFrame.getJMenuBar(),switchToGivenPanel,
                                                    studentCourseGrade));
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

        this.groupSwitch = new GroupSwitch() {
            @Override
            public void switchToGroupPage() {
                setNewPage(yearHolderPage);
            }
        };

        this.courseInfoTransfer = new CourseInfoTransfer() {
            @Override
            public CourseInfo getCourseInfoByName(String name) {
                return coursesPage.getCourseByName(name);
            }
        };

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
            public boolean addClass(ClassInfo classInfo) {
                return yearHolderPage.addNewClassToPage(classInfo);
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
            public void startAddStudentTeacherToCourse(String courseName, CourseGoalModel courseGoalModel, CourseGradeModel courseGradeModel, List<ClassInfo> classInfo) {
                setNewPage(new AddStudentTeacherToCourse(personLexicon, rePackWindow,courseName,
                            courseGoalModel,courseGradeModel,classInfo,switchToStudentCourseGrade));
            }

            @Override
            public void startChooseGroupPage(String courseName, CourseGoalModel courseGoalModel, CourseGradeModel courseGradeModel) {
                setNewPage(new ChooseGroupPage(yearHolderPage.getClasses(), switchToAddStudentTeacherToCourse, courseName,
                        courseGoalModel, courseGradeModel, mainFrame.getJMenuBar()));
            }
        };

        this.switchToCreateFromCourse = new SwitchToCreateFromCourse() {
            @Override
            public void switchToPage() {
                setNewPage(new CreateFromCourse(coursesPage,switchToAddStudentTeacherToCourse));
            }
        };



        setLayout(new BorderLayout());

        addMenu();
        logger.log(Level.INFO,"Added all menu items successfully.");

        logger.log(Level.INFO, "Starting to load data from files.");
        startLoad();
        logger.log(Level.INFO, "Done loading data from files.");

        logger.log(Level.INFO, "Setting first Panel (FrontPage).");
        setNewPage(frontPage);

        this.pack();
        this.setVisible(true);
    }

    private void startLoad() {
        try {
            settingsLoader = new SettingsLoader(new BufferedInputStream(new FileInputStream("content/Settings.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            personLexicon = new PersonLexicon(new BufferedInputStream(new FileInputStream("content/Students.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            yearHolderPage = new YearHolderPage(new BufferedInputStream(new FileInputStream("content/Groups.txt")),
                    personLexicon,studentClicked,rePackWindow);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            yearHolderPage = new YearHolderPage(new ArrayList<>(),studentClicked,rePackWindow);
        }

        try {
            coursesPage = new CoursesPage(new BufferedInputStream(new FileInputStream("content/Courses.txt")),
                    personLexicon,groupDataTransfer,switchToStudentCourseGrade,mainFrame.getJMenuBar());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setupContentDir() {
        File contentDir = new File("content");

        if (!contentDir.exists()) {
            contentDir.mkdir();
            if (contentDir.exists()) {
                System.out.println("direktory creted");
            }
        }
        if (contentDir.exists()) {
            try {
                File studentsFile = new File("content/Students.txt");
                if (!studentsFile.exists()) {
                    studentsFile.createNewFile();
                    System.out.println("Students.txt created");
                }
                File groupsFile = new File("content/Groups.txt");
                if (!groupsFile.exists()) {
                    groupsFile.createNewFile();
                    System.out.println("Groups.txt created");
                }
                File coursesFile = new File("content/Courses.txt");
                if (!coursesFile.exists()) {
                    coursesFile.createNewFile();
                    System.out.println("course.txt created");
                }
                File settingsFile = new File("content/Settings.txt");
                if (!settingsFile.exists()) {
                    settingsFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void setNewPage(main.Interfaces.Panel panel) {
        Panel currentPanel = panelHistoryStore.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.clearMenuBar(mainFrame.getJMenuBar());
        }
        panelHistoryStore.addPanel(panel);
        panel.setupMenuBar(mainFrame.getJMenuBar());
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

        JMenuItem frontPageItem = new JMenuItem();
        frontPageItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(frontPage);
            }
        });
        frontPageItem.setText("Front-Page");
        menu.add(frontPageItem);

        JMenuItem showYearHolderPage = new JMenuItem();
        showYearHolderPage.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(yearHolderPage);
            }
        });
        showYearHolderPage.setText("Groups");
        menu.add(showYearHolderPage);

        JMenuItem showCoursePage = new JMenuItem();
        showCoursePage.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(coursesPage);
            }
        });
        showCoursePage.setText("Course-Page");
        menu.add(showCoursePage);

        JMenuItem showAllStudents = new JMenuItem();
        showAllStudents.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(new ShowAllStudents(mainFrame.getJMenuBar(),personLexicon,rePackWindow,courseInfoTransfer,
                                                switchToStudentCourseGrade, settingsLoader));
            }
        });
        showAllStudents.setText("Show Students");
        menu.add(showAllStudents);

        jMenuBar.add(menu);
    }

    private void addCreateMenu(JMenuBar jMenuBar) {
        JMenu menu = new JMenu("Create");

        JMenuItem createCourse = new JMenuItem();
        createCourse.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewPage(new CreateCourse(rePackWindow,switchToAddStudentTeacherToCourse,switchToCreateFromCourse,coursesPage));
            }
        });
        createCourse.setText("Create Course");
        menu.add(createCourse);

        JMenuItem createYearClass = new JMenuItem();
        createYearClass.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateYearClass createYearClass = new CreateYearClass(personLexicon, addToYearHolderPage, groupSwitch);
                setNewPage(createYearClass);
            }
        });
        createYearClass.setText("Create Group");
        menu.add(createYearClass);

        JMenuItem studentCreation = new JMenuItem();
        studentCreation.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PersonCreation sc = new PersonCreation(student -> System.out.println(student.getFirstName()),personLexicon, settingsLoader);
                setNewPage(sc);
            }
        });
        studentCreation.setText("Create New Person");
        menu.add(studentCreation);

        jMenuBar.add(menu);
    }
}
