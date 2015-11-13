package main;

import java.util.List;

/**
 * Created by fumoffu on 2015-10-20.
 */
public class Teacher  {
    private String firstName;
    private List<String> name;
    private String lastName;
    private String cellPhoneNumber;
    private String emailAdress;

    public Teacher(final List<String> name, final String lastName, final String cellPhoneNumber, final String emailAdress,
		   final int firstName) {
	this.name = name;
	this.lastName = lastName;
	this.cellPhoneNumber = cellPhoneNumber;
	this.emailAdress = emailAdress;
	this.firstName = name.get(firstName);
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
    }

    public void setLastName(final String lastName) {
	this.lastName = lastName;
    }

    public void setCellPhoneNumber(final String cellPhoneNumber) {
	this.cellPhoneNumber = cellPhoneNumber;
    }

    public void setEmailAdress(final String emailAdress) {
	this.emailAdress = emailAdress;
    }

    public void setFirstName(final int nameIndex) {
	firstName = name.get(nameIndex);
    }
}
