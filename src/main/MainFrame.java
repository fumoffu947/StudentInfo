package main;

import main.InterfaceDataTransfer.StudentClicked;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by fumoffu on 2015-10-20.
 *
 * (separeat adress sida)
 * separeat sida för avarje elev för varja kurs
 */
public class MainFrame extends JFrame
{

    public static void main(String[] args) {
	new MainFrame();
    }

    public MainFrame(){
	super("The InfoPager");
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	addMenu();

	addExampleTextFields();

	exampleClassInfoPage();
	/*
	ArrayList<String> l = new ArrayList<>();
	l.add("Niclas");
	Student s = new Student(l,"sture",0,"073000000","@hotmail.com");
	this.setContentPane(s.getjPanel()); // */

	//*
	StudentCreation sc = new StudentCreation(student -> System.out.println(student.getFirstName()));
	//this.setContentPane(sc.getjPanel());

	this.pack();
	this.setVisible(true);
    }

    private void exampleClassInfoPage() {
	ArrayList<Student> students = new ArrayList<>();
	ArrayList<String> name = new ArrayList<>();
	name.add("aaaa");
	name.add("aaaa");
	students.add(new Student(name, "aaab", 0, "aaaa", "aaaa"));
	students.add(new Student(name, "aabb", 0, "aaaa", "aaaa"));
	students.add(new Student(name, "abbb", 0, "aaaa", "aaaa"));
	students.add(new Student(name, "bbbb", 0, "aaaa", "aaaa"));

	ClassInfo ca1 = new ClassInfo(students, "åk1");
	ClassInfo ca2 = new ClassInfo(students, "åk2");
	ClassInfo ca3 = new ClassInfo(students, "åk3");

	ArrayList<ClassInfo> list = new ArrayList<>();
	list.add(ca1);
	list.add(ca2);
	list.add(ca3);

	YearHolderPage yHP = new YearHolderPage(list, new StudentClicked()
	{
	    @Override public void studentData(final String name, final String lastName) {
		System.out.println( name + " : " + lastName);
	    }
	});

	this.setContentPane(yHP.getClassInfoPanel());
    }

    private void addExampleTextFields() {
	final JTextField textArea = new JTextField("hihi",20);
	textArea.setBorder(new LineBorder(Color.BLACK));
	JTextField textArea2 = new JTextField(20);
	JPanel panel = new JPanel();
	panel.add(textArea);
	panel.add(textArea2);
	this.setContentPane(panel);

	final JTextField p = (JTextField) panel.getComponent(0);
	System.out.println(p.getText());
    }

    private void addMenu() {
	JMenu menu = new JMenu("ALT");
	JMenuItem exit = new JMenuItem("Exit");
	menu.add(exit);
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(menu);

	this.setJMenuBar(menuBar);
    }
}
