package main.DataStore;

import main.Comparators.ClassInfoComparator;
import main.Interfaces.*;
import main.Interfaces.InterfaceDataTransfer.StudentClicked;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fumoffu on 2015-10-24.
 */
public class YearHolderPage implements main.Interfaces.Panel {
    private final RePackWindow rePackWindow;
    private List<ClassInfo> classes;
    private JPanel pageHolder = new JPanel(new BorderLayout());
    private JPanel classContainer = new JPanel(new GridBagLayout());
    private StudentClicked studentClicked;
    private List<DefaultTableModel> tableModels = new ArrayList<>();
    private int tableNr = 0;


    public YearHolderPage(final List<ClassInfo> classes, StudentClicked studentClicked, RePackWindow rePackWindow) {
        this.classes = classes;
        this.classes.sort(new ClassInfoComparator());
        this.studentClicked = studentClicked;
        this.rePackWindow = rePackWindow;
        setupPanel();
    }

    private void setupPanel() {
        for (ClassInfo aClass : classes) {

            addClassToPage(aClass);
        }


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
                classes.remove(aClass);
                classContainer.remove(jScrollPane);
                classContainer.remove(removeClassButton);
                rePackWindow.rePackWindow();
            }
        });
        removeClassButton.setText("Remove Class");

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


