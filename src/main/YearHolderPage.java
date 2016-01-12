package main;

import main.Comparators.ClassInfoComparator;
import main.Interfaces.InterfaceDataTransfer.StudentClicked;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by fumoffu on 2015-10-24.
 */
public class YearHolderPage
{
    private List<ClassInfo> classes;
    private JPanel classInfoPanel = new JPanel();
    private StudentClicked studentClicked;


    public YearHolderPage(final List<ClassInfo> classes, StudentClicked studentClicked) {
	this.classes = classes;
	this.classes.sort(new ClassInfoComparator());
	this.studentClicked = studentClicked;
	setupPanel();
    }

    private void setupPanel() {
	for (ClassInfo aClass : classes) {

	    JPanel panel = new JPanel();
	    panel.setBorder(new LineBorder(Color.BLACK));
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    final JTextField field = new JTextField();
	    field.setHorizontalAlignment(JTextField.CENTER);
	    field.setFont(new Font("Times New Roman", Font.BOLD, 15));
	    field.setText(aClass.getClassName());
	    System.out.println(aClass.getClassName());
	    field.setEditable(false);
	    panel.add(field);

	    for (Student student : aClass.getStudents()) {

		String studentName = student.getFirstName() + " " + student.getLastName();
		JTextField textField = new JTextField(studentName);
		textField.setEditable(false);

		panel.add(textField);

		textField.addMouseListener(new MouseInputAdapter()
		{
		    @Override public void mouseClicked(final MouseEvent e) {
			super.mouseClicked(e);
			studentClicked.studentData(student.getFirstName(), student.getLastName());
		    }
		});
	    }
	    classInfoPanel.add(panel);
	}
    }

    public JPanel getClassInfoPanel() {
	return classInfoPanel;
    }
}
