package main.Creation.CourseCreation;

import main.DataStore.CourseGoalModel;
import main.Interfaces.*;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * An example to get me going
 *
 * DefaultTableModel tableModel = new DefaultTableModel();
 * tableModel.addColumn("FirstName");
 * tableModel.addColumn("LastName");
 * tableModel.addColumn("Sport");
 * tableModel.addColumn("# of Years");
 * tableModel.addColumn("Vegetarian");
 *
 * tableModel.addRow(new Object[] {"Kathy", "Smith",
 * "Snowboarding", new Integer(5), new Boolean(false)});
 * tableModel.addRow(new Object[] {"John", "Doe",
 * "Rowing", new Integer(3), new Boolean(true)});
 * tableModel.addRow(new Object[] {"Sue", "Black",
 * "Knitting", new Integer(2), new Boolean(false)});
 * tableModel.addRow(new Object[] {"Jane", "White",
 * "Speed reading", new Integer(20), new Boolean(true)});
 * tableModel.addRow(new Object[] {"Joe", "Brown",
 * "Pool", new Integer(10), new Boolean(false)});
 *
 * final JTable table = new JTable(tableModel);
 * table.setPreferredScrollableViewportSize(new Dimension(500, 70));
 * table.setFillsViewportHeight(true);
 * tableModel.addColumn("troll");
 * System.out.println(tableModel.getColumnCount() + " " + tableModel.getRowCount());
 * tableModel.setValueAt("trolls",0,tableModel.getColumnCount()-1);
 * this.setContentPane(table);
 */
public class CreateCourse implements main.Interfaces.Panel {

	private final RePackWindow rePackWindow;
	private final SwitchToAddStudentTeacherToCourse switchPanel;
	private JTextField courseName = new JTextField();
    private JPanel pageHolder = new JPanel();
	private DefaultTableModel tableModel = new DefaultTableModel();
	private JTable table = new JTable(tableModel);
	private JScrollPane jScrollPane = new JScrollPane(table);

	private JButton addActivityButton = new JButton();
	private JButton addGoalButton = new JButton();
	private JButton continueToNextStep = new JButton();


    public CreateCourse(RePackWindow rePackWindow, JMenuBar jMenuBar, SwitchToAddStudentTeacherToCourse switchPanel) {
		this.rePackWindow = rePackWindow;
		this.switchPanel = switchPanel;
		tableModel.addColumn("GoalColumn");
		tableModel.addColumn("Activity 1");
		tableModel.addRow(new Object[] {});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


		setUpButtons(rePackWindow, jMenuBar);

		setUpLayoutAndPanels();
	}

	private void setUpLayoutAndPanels() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.insets = new Insets(0,0,0,0);
		c1.weightx = 1.0;
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		c2.gridy = 1;
		c2.weightx = 1.0;
		c2.weighty = 1.0;
		c2.insets = new Insets(0,0,0,0);
		pageHolder.setLayout(gridBagLayout);
		pageHolder.add(courseName,c1);
		pageHolder.add(jScrollPane,c2);
	}

	private void setUpButtons(final RePackWindow rePackWindow, JMenuBar jMenuBar) {
		addActivityButton.setAction(new AbstractAction()
		{
	    	@Override public void actionPerformed(final ActionEvent e) {
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}
				tableModel.addColumn("Activity "+table.getColumnCount());
				rePackWindow.rePackWindow();

	    	}
		});
		addActivityButton.setText("Add Activity");
		jMenuBar.add(addActivityButton);


		addGoalButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] {});
				rePackWindow.rePackWindow();
			}
		});
		addGoalButton.setText("Add Goal");
		jMenuBar.add(addGoalButton);

		continueToNextStep.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}

				String name = courseName.getText();

				ArrayList<String> goals = new ArrayList<>();
				ArrayList<String> activities = new ArrayList<>();

				if (table.getColumnCount() > 1 && table.getRowCount() > 1) {

					for (int col = 1; col < table.getColumnCount(); col++) {
						String arg = (String) table.getValueAt(0, col);
						if (arg != null) {
							activities.add(arg);
						}
					}

					for (int row = 0; row < table.getRowCount(); row++) {
						String arg = (String) table.getValueAt(row, 0);
						if (arg != null) {
							goals.add(arg);
						}
					}
				}
				if (!activities.isEmpty() && !goals.isEmpty() && name.length() > 0) {
					clearMenuBar(jMenuBar);
					rePackWindow.rePackWindow();
					switchPanel.startAddStudentTeacherToCourse(name, new CourseGoalModel(goals, activities));
				}
				rePackWindow.rePackWindow();
			}
		});
		continueToNextStep.setText("Continue");
		jMenuBar.add(continueToNextStep);
	}

	@Override
	public void clearMenuBar(JMenuBar jMenuBar) {
		jMenuBar.remove(addActivityButton);
		jMenuBar.remove(addGoalButton);
		jMenuBar.remove(continueToNextStep);
	}

	@Override
	public void setupMenuBar(JMenuBar jMenuBar) {
		setUpButtons(rePackWindow, jMenuBar);
	}

	@Override
    public JPanel getPageHolder() {
	return pageHolder;
    }
}

