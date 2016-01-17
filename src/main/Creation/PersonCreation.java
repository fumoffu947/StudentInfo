package main.Creation;

import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.Student;
import main.DataStore.Teacher;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.StudentCreationReturn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by fumoffu on 2015-10-22.
 */
public class PersonCreation implements main.Interfaces.Panel
{
	private int nextID;
	private JPanel pageHolder = new JPanel();
    private JTextField nameField = new JTextField(20);
    private JTextField lastNameField = new JTextField(20);
    private JTextField cellNumber = new JTextField(20);
    private JTextField email = new JTextField(20);
	private JCheckBox isTeacherCheckBox = new JCheckBox();

    public PersonCreation(StudentCreationReturn scr, int nextID, PersonLexicon personLexicon) {
		this.nextID = nextID;
		pageHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel nameContainer = new JPanel();
		JPanel surnameContainer = new JPanel();
		JPanel cellNumberContainer = new JPanel();
		JPanel emailContainer = new JPanel();
		JPanel isTeacherContainer = new JPanel();

		nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.Y_AXIS));
		final JTextField name = new JTextField("Name");
		name.setEditable(false);
		nameContainer.add(name);
		nameContainer.add(nameField);

		surnameContainer.setLayout(new BoxLayout(surnameContainer, BoxLayout.Y_AXIS));
		final JTextField lastname = new JTextField("Surname");
		lastname.setEditable(false);
		surnameContainer.add(lastname);
		surnameContainer.add(lastNameField);

		cellNumberContainer.setLayout(new BoxLayout(cellNumberContainer, BoxLayout.Y_AXIS));
		final JTextField cpN = new JTextField("Cellphone Number");
		cpN.setEditable(false);
		cellNumberContainer.add(cpN);
		cellNumberContainer.add(cellNumber);

		emailContainer.setLayout(new BoxLayout(emailContainer, BoxLayout.Y_AXIS));
		final JTextField emailadress = new JTextField("E-mail address");
		emailadress.setEditable(false);
		emailContainer.add(emailadress);
		emailContainer.add(email);

		isTeacherContainer.setLayout(new BoxLayout(isTeacherContainer, BoxLayout.Y_AXIS));
		final JTextField isTeacherTextField = new JTextField("Is teacher?");
		isTeacherTextField.setEditable(false);
		isTeacherContainer.add(isTeacherTextField);
		isTeacherContainer.add(isTeacherCheckBox);

		pageHolder.add(nameContainer);
		pageHolder.add(surnameContainer);
		pageHolder.add(cellNumberContainer);
		pageHolder.add(emailContainer);
		pageHolder.add(isTeacherContainer);
		final JButton createButton = new JButton();
		pageHolder.add(createButton);

		initButton(scr, createButton, personLexicon);
    }

    private void initButton(final StudentCreationReturn scr, final JButton createButton, PersonLexicon personLexicon) {
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
			final String emailAddress = email.getText();
			if (lastname != null && cellPhoneNumber != null && emailAddress != null && !names.isEmpty()) {
				Student student;
				if (isTeacherCheckBox.isSelected()) {
					student = new Teacher(names,lastname, 0, cellPhoneNumber, emailAddress, nextID);
				} else {
					student = new Student(names, lastname, 0, cellPhoneNumber, emailAddress, nextID);
				}
				Collection<Person> personsCollection = personLexicon.getPersonsByName(student.getFirstName());
				if (!personsCollection.isEmpty()) {
					int samePerson = 0;
					for (Person p: personsCollection) {
						if (student.toString().equals(p.toString())) {
							String newLine = System.getProperty("line.separator");
							samePerson = JOptionPane.showConfirmDialog((Component)null,"A person by the same name, midle names, surname and age"+
									newLine + " was found do you still wish to add this new person?","Warning Person Exists",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							break;
						}
					}
					if (samePerson == 0) {
						boolean a = personLexicon.insertPersonToLexicon(student.getFirstName(), student);
						scr.returnStudent(student);
						nextID++;
					}
				} else {
					personLexicon.insertPersonToLexicon(student.getFirstName(), student);
					scr.returnStudent(student);
					nextID++;
				}
			}
			nameField.setText("");
			lastNameField.setText("");
			cellNumber.setText("");
			email.setText("");

			}
		});
		createButton.setText("Add Student");
    }

    public JPanel getPageHolder() {
	return pageHolder;
    }

	@Override
	public void clearMenuBar(JMenuBar jMenuBar) {

	}

	@Override
	public void setupMenuBar(JMenuBar jMenuBar) {

	}
}
