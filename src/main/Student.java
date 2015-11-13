package main;

import javax.swing.*;
import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 *
 * (adress)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class Student {

    private JPanel jPanel;
    private String firstName;
    private List<String> name;
    private String lastName;
    private String cellPhoneNumber;
    private String emailAdress;

    public Student(final List<String> name, final String lastName, final int firstName, final String cellPhoneNumber,
                   final String emailAdress) {
	this.name = name;
	this.lastName = lastName;
	this.firstName = name.get(firstName);
        this.cellPhoneNumber = cellPhoneNumber;
        this.emailAdress = emailAdress;

	createJPanel(name, lastName, cellPhoneNumber, emailAdress);
    }

    private void createJPanel(final List<String> name, final String lastName, final String cellPhoneNumber,
			      final String emailAdress)
    {
	jPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextField lastNameField = new JTextField(lastName);
	JTextField cellNumber = new JTextField(cellPhoneNumber);
	JTextField email = new JTextField(emailAdress);
	StringBuilder stringBuilder = new StringBuilder(" ");
	for (String s : name) {
	    stringBuilder.append(" ").append(s);
	}
	nameField.setText(stringBuilder.toString());

	nameField.setEditable(false);
	lastNameField.setEditable(false);
	cellNumber.setEditable(false);
	email.setEditable(false);
	jPanel.add(nameField);
	jPanel.add(lastNameField);
	jPanel.add(cellNumber);
	jPanel.add(email);
    }

    public String getFirstName() {
	return firstName;
    }

    public List<String> getName() {
	return name;
    }

    public String getLastName() {
	return lastName;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setName(final List<String> name) {
	this.name = name;
	firstName = name.get(0);
	StringBuilder sb = new StringBuilder();
	for (String s : name) {
	    sb.append(" ").append(s);
	}
	JTextField t = (JTextField) jPanel.getComponent(0);
	t.setText(sb.toString());
    }

    public void setLastName(final String lastName) {
	this.lastName = lastName;
	JTextField t = (JTextField) jPanel.getComponent(1);
	t.setText(lastName);
    }

    public void setFirstName(final int index) {
	firstName = name.get(index);
    }

    public void setCellPhoneNumber(final String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
	JTextField t = (JTextField) jPanel.getComponent(2);
	t.setText(cellPhoneNumber);
    }

    public void setEmailAdress(final String emailAdress) {
        this.emailAdress = emailAdress;
	JTextField t = (JTextField) jPanel.getComponent(3);
	t.setText(lastName);
    }

    public JPanel getjPanel() {
	return jPanel;
    }
}
