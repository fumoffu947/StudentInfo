package main;

import main.InterfaceDataTransfer.StudentCreationReturn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by fumoffu on 2015-10-22.
 */
public class StudentCreation
{
    private JPanel jPanel = new JPanel();
    private JTextField nameField = new JTextField(20);
    private JTextField lastNameField = new JTextField(20);
    private JTextField cellNumber = new JTextField(20);
    private JTextField email = new JTextField(20);

    public StudentCreation(StudentCreationReturn scr) {
	jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	JPanel nameContainer = new JPanel();
	JPanel lastnameContainer = new JPanel();
	JPanel cellNumberContainer = new JPanel();
	JPanel emailContainer = new JPanel();

	nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.Y_AXIS));
	final JTextField name = new JTextField("Name");
	name.setEditable(false);
	nameContainer.add(name);
	nameContainer.add(nameField);

	lastnameContainer.setLayout(new BoxLayout(lastnameContainer, BoxLayout.Y_AXIS));
	final JTextField lastname = new JTextField("Lastname");
	lastname.setEditable(false);
	lastnameContainer.add(lastname);
	lastnameContainer.add(lastNameField);

	cellNumberContainer.setLayout(new BoxLayout(cellNumberContainer, BoxLayout.Y_AXIS));
	final JTextField cpN = new JTextField("Cellphone Number");
	cpN.setEditable(false);
	cellNumberContainer.add(cpN);
	cellNumberContainer.add(cellNumber);

	emailContainer.setLayout(new BoxLayout(emailContainer, BoxLayout.Y_AXIS));
	final JTextField emailadress = new JTextField("Emailadress");
	emailadress.setEditable(false);
	emailContainer.add(emailadress);
	emailContainer.add(email);

	jPanel.add(nameContainer);
	jPanel.add(lastnameContainer);
	jPanel.add(cellNumberContainer);
	jPanel.add(emailContainer);
	final JButton createButton = new JButton("Add Student");
	jPanel.add(createButton);

	initButton(scr, createButton);
    }

    private void initButton(final StudentCreationReturn scr, final JButton createButton) {
	createButton.setAction(new AbstractAction()
	{
	    @Override public void actionPerformed(final ActionEvent e) {
		String[] namesArray = nameField.getText().split(" ");
		ArrayList<String> names = new ArrayList<>();
		for (String name : namesArray) {
		    if (!name.equals(" ") || !name.equals(" ") && name.contains("[a-zA-Z]")) {
			names.add(name);
		    }
		}
		final String lastname = lastNameField.getText();
		final String cellPhoneNumber = cellNumber.getText();
		final String emailAdress = email.getText();
		if (lastname != null && cellPhoneNumber != null && emailAdress != null && !names.isEmpty()) {
		    Student student = new Student(names, lastname, 0, cellPhoneNumber, emailAdress);
		    scr.returnStudent(student);
		}
		nameField.setText("");
		lastNameField.setText("");
		cellNumber.setText("");
		email.setText("");

	    }
	});
    }

    public JPanel getjPanel() {
	return jPanel;
    }
}
