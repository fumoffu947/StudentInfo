package main;

import main.Creation.CourseCreation.AddStudentTeacherToCourse;
import main.Creation.CourseCreation.CreateCourse;
import main.Creation.CreateYearClass;
import main.Creation.PersonCreation;
import main.DataStore.*;
import main.DataStore.Lexicon.PersonLexicon;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.AddToYearHolderPage;
import main.Interfaces.InterfaceDataTransfer.StudentClicked;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.Panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class MainFrame extends JFrame {

    private final AddToYearHolderPage addToYearHolderPage;
    private final StudentClicked studentClicked;
    private final SwitchToAddStudentTeacherToCourse switchToAddStudentTeacherToCourse;
    private RePackWindow rePackWindow;
    private PersonLexicon personLexicon = new PersonLexicon();
    private YearHolderPage yearHolderPage;
    private PanelHistoryStore panelHistoryStore = new PanelHistoryStore();
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
        this.rePackWindow = () -> {
            mainFrame.setMinimumSize(mainFrame.getSize());
            mainFrame.pack();
            mainFrame.repaint();
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
            public void startAddStudentTeacherToCourse(String CourseName, CourseGoalModel courseGoalModel) {
                setNewPage(new AddStudentTeacherToCourse(personLexicon,mainFrame.getJMenuBar(), rePackWindow, courseGoalModel));
            }
        };

        setLayout(new BorderLayout());

        addMenu();

        addExampleTextFields();

        exampleClassInfoPage();



		/*
        ArrayList<String> l = new ArrayList<>();
		l.add("Nicolas");
		Student s = new Student(l,"stare",0,"073000000","@hotmail.com");
		this.setContentPane(s.getPageHolder()); // */

        //*


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
        System.out.println(personLexicon.insertPersonToLexicon(s2.getFirstName(), s2));
        System.out.println(personLexicon.containsPrefix("a"));
        System.out.println(personLexicon.containsPrefix("aa"));
        System.out.println(personLexicon.containsPrefix("aaa"));
        System.out.println(personLexicon.containsPrefix("aaaa"));
        System.out.println(personLexicon.containsPrefix("aaaaa"));
        Collection<Person> collection = personLexicon.getPersonsByName("aaaa");
        for (Person p : collection) {
            if (!p.isTeacher()) {
                System.out.println(p.toString());

            }
        }

        //AddStudentTeacherToCourse addStudentTeacherToCourse = new AddStudentTeacherToCourse(personLexicon, 	getJMenuBar(),rePackWindow);
        //this.setContentPane(addStudentTeacherToCourse.getPageHolder());


        Collection<Student> coll = new ArrayList<>();
        coll.add(s);
        coll.add(s2);
        new PanelHistoryStore();

        ArrayList<Integer> sg1 = new ArrayList<>();
        sg1.add(1);
        sg1.add(1);
        sg1.add(1);
        sg1.add(1);
        ArrayList<ArrayList<Integer>> grade = new ArrayList<>();
        grade.add(sg1);
        grade.add(sg1);
        grade.add(sg1);

        System.out.println(personLexicon.insertStudentGrade(s,new StudentGrade(grade),"Test"));
        System.out.println(personLexicon.insertStudentGrade(s2,new StudentGrade(grade),"Test"));

        ClassInfo cI = new ClassInfo(new ArrayList<Student>(coll),"Test");
        CourseGoalModel cGM = new CourseGoalModel(new ArrayList<String>(Arrays.asList("test1","test2","test3")), new ArrayList<>(Arrays.asList("partgoal1", "partgoal2","partgoal1", "partgoal2")));
        CourseInfo c = new CourseInfo(cI,new ArrayList<Student>(),"Test",new ArrayList<Teacher>(),cGM);

        setContentPane(new StudentCourseGrade(c,personLexicon, rePackWindow).getPageHolder());
        this.pack();
        this.setVisible(true);
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


        yearHolderPage = new YearHolderPage(list, studentClicked);
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
