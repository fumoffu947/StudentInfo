package main.Creation;

import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.SettingsLoader;
import main.DataStore.Student;
import main.DataStore.Teacher;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.StudentCreationReturn;
import main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fumoffu on 2015-10-22.
 */
public class PersonCreation implements main.Interfaces.Panel
{
    private Logger logger = MainFrame.logger;

    private final SettingsLoader settingsLoader;
    private JPanel pageHolder = new JPanel();
    private JTextField nameField = new JTextField(20);
    private JTextField lastNameField = new JTextField(20);
    private JTextField cellNumber = new JTextField(20);
    private JTextField email = new JTextField(20);
    private JCheckBox isTeacherCheckBox = new JCheckBox();

    public PersonCreation(StudentCreationReturn scr, PersonLexicon personLexicon, SettingsLoader settingsLoader) {
        this.settingsLoader = settingsLoader;
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
        final JTextField surname = new JTextField("Surname");
        surname.setEditable(false);
        surnameContainer.add(surname);
        surnameContainer.add(lastNameField);

        cellNumberContainer.setLayout(new BoxLayout(cellNumberContainer, BoxLayout.Y_AXIS));
        final JTextField cpN = new JTextField("Cellphone Number");
        cpN.setEditable(false);
        cellNumberContainer.add(cpN);
        cellNumberContainer.add(cellNumber);

        emailContainer.setLayout(new BoxLayout(emailContainer, BoxLayout.Y_AXIS));
        final JTextField emailAddress = new JTextField("E-mail address");
        emailAddress.setEditable(false);
        emailContainer.add(emailAddress);
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
                boolean gotScattered = true;
                int nextID = settingsLoader.getAScatterdIDNumber();
                if (nextID == -1) {
                    nextID = settingsLoader.getLastIDNumber();
                    gotScattered = false;
                }
                String[] namesArray = nameField.getText().split(" ");
                ArrayList<String> names = new ArrayList<>();
                for (String name : namesArray) {
                    if (!name.equals(" ") || !name.equals(" ") && name.contains("[a-zA-Z]")) {
                        names.add(name);
                    }
                }
                final String surname = lastNameField.getText();
                final String cellPhoneNumber = cellNumber.getText();
                final String emailAddress = email.getText();
                if (surname != null && surname.length() > 1 && cellPhoneNumber != null && emailAddress != null && !names.isEmpty()) {
                    Student student;
                    if (isTeacherCheckBox.isSelected()) {
                        student = new Teacher(names,surname, 0, cellPhoneNumber, emailAddress, nextID);
                    } else {
                        student = new Student(names, surname, 0, cellPhoneNumber, emailAddress, nextID);
                    }
                    Collection<Person> personsCollection = personLexicon.getPersonsByName(student.getFirstName());
                    if (!personsCollection.isEmpty()) {
                        int samePerson = 0;
                        for (Person p: personsCollection) {
                            if (student.toString().equals(p.toString())) {
                                String newLine = System.getProperty("line.separator");
                                samePerson = JOptionPane.showConfirmDialog((Component)null,"A person by the same name, middle names and surname "+
                                        newLine + " was found do you still wish to add this new person?","Warning Person Exists",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                break;
                            }
                        }
                        if (samePerson == 0) {
                            boolean successfulInsert = personLexicon.insertPersonToLexicon(student.getFirstName(), student);
                            if (successfulInsert) {
                                scr.returnStudent(student);
                                if (!gotScattered) {
                                    settingsLoader.setLastIDNumber(nextID+1);
                                }
                                logger.log(Level.INFO,"The person: "+student+" was created");
                                JOptionPane.showMessageDialog(null, "A person by name : "+student+" was created.","Person Creation",JOptionPane.INFORMATION_MESSAGE);
                            }else {
                                JOptionPane.showMessageDialog(null,"An error occurred and the person was not created.",
                                        "Person Creation",JOptionPane.WARNING_MESSAGE);
                            }

                        }
                    } else {
                        personLexicon.insertPersonToLexicon(student.getFirstName(), student);
                        logger.log(Level.INFO,"The person: "+student+" was created");
                        JOptionPane.showMessageDialog(null, "A person by name : "+student+" was created.","Person Creation",JOptionPane.INFORMATION_MESSAGE);
                        scr.returnStudent(student);
                        if (!gotScattered) {
                            settingsLoader.setLastIDNumber(nextID+1);
                        }
                    }
                }else {
                    String newLine = System.getProperty("line.separator");
                    JOptionPane.showMessageDialog(null, "Person was not created."+newLine+
                            "The name field must me filled with at least one name."+newLine+
                            "Surname must be of at least two characters.","Person Creation",JOptionPane.INFORMATION_MESSAGE);
                }
                nameField.setText("");
                lastNameField.setText("");
                cellNumber.setText("");
                email.setText("");

            }
        });
        createButton.setText("Add Person");
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
