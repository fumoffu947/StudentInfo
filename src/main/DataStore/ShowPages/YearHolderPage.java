package main.DataStore.ShowPages;

import main.Comparators.ClassInfoComparator;
import main.DataStore.ClassInfo;
import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.Student;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.StudentClicked;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

/**
 * Created by fumoffu on 2015-10-24.
 */
public class YearHolderPage implements main.Interfaces.Panel {

    private final RePackWindow rePackWindow;
    private StudentClicked studentClicked;
    private List<ClassInfo> classes;

    private JPanel pageHolder = new JPanel(new BorderLayout());
    private JPanel classContainer = new JPanel(new GridBagLayout());

    private int tableNr = 0;
    private boolean startSetup = true;


    public YearHolderPage(final List<ClassInfo> classes, StudentClicked studentClicked, RePackWindow rePackWindow) {
        this.classes = classes;
        classes.add(0,new ClassInfo(new ArrayList<>(),"No Class"));
        this.classes.sort(new ClassInfoComparator());
        this.studentClicked = studentClicked;
        this.rePackWindow = rePackWindow;
        setupPanel();
    }

    public YearHolderPage(BufferedInputStream fileReader, PersonLexicon personLexicon, StudentClicked studentClicked, RePackWindow rePackWindow) {
        this.classes = new ArrayList<>();
        classes.add(new ClassInfo(new ArrayList<>(),"No Class"));
        this.studentClicked = studentClicked;
        this.rePackWindow = rePackWindow;

        try {
            int character = fileReader.read();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while(character != -1) {
                if ('[' == character) {
                    if (byteArrayOutputStream.size() != 0) {
                        String personString = new String(Base64.getDecoder().decode(byteArrayOutputStream.toByteArray()));
                        loadGroupFromString(personString.split(","), personLexicon);
                        byteArrayOutputStream.reset();
                    }
                }else {
                    byteArrayOutputStream.write(character);
                }

                character = fileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        setupPanel();
    }

    private void loadGroupFromString(String[] groupsInfo, PersonLexicon personLexicon) {
        String groupName = groupsInfo[0];
        ArrayList<Student> students = new ArrayList<>();
        for (int personIndex = 1; personIndex < groupsInfo.length; personIndex++) {
            String[] personInfo = groupsInfo[personIndex].split(" ");
            Collection<Person> foundPersons = personLexicon.getPersonsByName(personInfo[0]);
            for (Person person : foundPersons) {
                if (person.getID() == Integer.parseInt(personInfo[1]) && !person.isTeacher()) {
                    students.add((Student) person);
                }
            }
        }
        System.out.println("adding a course");
        if (!groupName.equals("No Class")) {
            classes.add(new ClassInfo(students, groupName));
        }
    }

    private void setupPanel() {
        System.out.println("setting up the panel");
        for (ClassInfo aClass : classes) {
            if (!aClass.getClassName().equals("No Class")) {
                addClassToPage(aClass);
            }
        }
        startSetup = false;


        pageHolder.add(new JScrollPane(classContainer));
    }

    public void addClassToPage(ClassInfo aClass) {
        DefaultTableModel tableModel = new DefaultTableModel();

        JTable table = new JTable(tableModel);
        table.setBorder(new LineBorder(Color.BLACK));
        table.setFont(new Font("Times New Roman", Font.BOLD, 15));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints tableConstraints = new GridBagConstraints();
        tableConstraints.gridx = tableNr;
        tableConstraints.gridy = 0;
        tableConstraints.weightx = 1.0;
        tableConstraints.weighty = 1.0;
        tableConstraints.insets = new Insets(10, 10, 0, 10);
        tableConstraints.fill = GridBagConstraints.BOTH;

        tableModel.addColumn(aClass.getClassName());

        for (Student student : aClass.getStudents()) {
            tableModel.addRow(new Object[]{student});
        }
        JScrollPane jScrollPane = new JScrollPane(table);
        classContainer.add(jScrollPane, tableConstraints);

        JButton removeClassButton = new JButton();
        removeClassButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null,"Are you sure that you want to remove this class?","Class Removal",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if (answer == JOptionPane.YES_OPTION) {
                    classes.remove(aClass);
                    classContainer.remove(jScrollPane);
                    classContainer.remove(removeClassButton);
                    rePackWindow.rePackWindow();
                }
            }
        });
        removeClassButton.setText("Remove Group");

        GridBagConstraints removeButtonConstraints = new GridBagConstraints();
        removeButtonConstraints.gridx = tableNr;
        removeButtonConstraints.gridy = 1;
        removeButtonConstraints.weightx = 1.0;
        removeButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        classContainer.add(removeClassButton, removeButtonConstraints);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (e.getValueIsAdjusting() && lsm.isSelectedIndex(i)) {
                        studentClicked.studentData(((Person) tableModel.getValueAt(i, 0)));
                    }
                }
            }
        });
        tableNr++;
        if (!startSetup) {
            classes.add(aClass);
        }
    }

    public ClassInfo getClassInfoByName(String name) {
        for (ClassInfo classInfo : classes) {
            if (classInfo.getClassName().equals(name)) {
                return classInfo;
            }
        }
        return null;
    }

    public JPanel getPageHolder() {
        return pageHolder;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    @Override
    public void clearMenuBar(JMenuBar jMenuBar) {

    }

    @Override
    public void setupMenuBar(JMenuBar jMenuBar) {

    }
}


