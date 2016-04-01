package main.DataStore;

import main.Interfaces.Person;

import javax.swing.*;
import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 *
 * (address)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class Student implements Person {

    private JPanel pageHolder;
    private int firstName;
    private List<String> name;
    private String lastName;
    private String cellPhoneNumber;
    private String emailAddress;
	private int ID;

    public Student(final List<String> name, final String lastName, final int firstName, final String cellPhoneNumber,
				   final String emailAddress, int ID) {

		assert name.size() > 0: "To few names was given.";
		assert firstName > -1: "First name can't be a negative number";

		this.name = name;
		this.lastName = lastName;
		this.firstName = firstName;
		this.cellPhoneNumber = cellPhoneNumber;
        this.emailAddress = emailAddress;
		this.ID = ID;

	createJPanel(name, lastName, cellPhoneNumber, emailAddress);
    }

    private void createJPanel(final List<String> name, final String lastName, final String cellPhoneNumber,
			      final String emailAddress)
    {
	pageHolder = new JPanel();
	JTextField nameField = new JTextField();
	JTextField lastNameField = new JTextField(lastName);
	JTextField cellNumber = new JTextField(cellPhoneNumber);
	JTextField email = new JTextField(emailAddress);
	StringBuilder stringBuilder = new StringBuilder(" ");
	for (String s : name) {
	    stringBuilder.append(" ").append(s);
	}
	nameField.setText(stringBuilder.toString());

	nameField.setEditable(false);
	lastNameField.setEditable(false);
	cellNumber.setEditable(false);
	email.setEditable(false);
	pageHolder.add(nameField);
	pageHolder.add(lastNameField);
	pageHolder.add(cellNumber);
	pageHolder.add(email);
    }

    public String getFirstName() {
	return name.get(firstName);
    }

	public int getFirstNameNumber() {
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setName(final List<String> name) {
	this.name = name;
	firstName = 0;
	StringBuilder sb = new StringBuilder();
	for (String s : name) {
	    sb.append(" ").append(s);
	}
	JTextField t = (JTextField) pageHolder.getComponent(0);
	t.setText(sb.toString());
    }

    public void setLastName(final String lastName) {
	this.lastName = lastName;
	JTextField t = (JTextField) pageHolder.getComponent(1);
	t.setText(lastName);
    }

    public void setFirstName(final int index) {
	firstName = index;
    }

    public void setCellPhoneNumber(final String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
	JTextField t = (JTextField) pageHolder.getComponent(2);
	t.setText(cellPhoneNumber);
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
	JTextField t = (JTextField) pageHolder.getComponent(3);
	t.setText(lastName);
    }

    public JPanel getPageHolder() {
	return pageHolder;
    }

	@Override
	public boolean isTeacher() {
		return false;
	}

	@Override
	public String toString() {
		String names = "";
		names += name.get(firstName) + " ";
		for (int i = 0; i < name.size(); i++) {
			if (i != firstName) {
				names += name.get(i) + " ";
			}
		}
		names += lastName;
		return names;
	}

	@Override
	public int getID() {
		return ID;
	}
}
