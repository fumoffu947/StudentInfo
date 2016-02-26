package main.Creation.CourseCreation;

import main.DataStore.CourseGoalModel;
import main.Interfaces.*;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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
	private JTextField courseNameWriteField = new JTextField();
    private JPanel pageHolder = new JPanel();
	private DefaultTableModel tableModel = new DefaultTableModel();
	private courseJTable table = new courseJTable(tableModel);
	private JScrollPane jScrollPane = new JScrollPane(table);

	private JButton addActivityButton = new JButton();
	private JButton addGoalButton = new JButton();
	private JButton continueToNextStep = new JButton();


    public CreateCourse(RePackWindow rePackWindow, JMenuBar jMenuBar, SwitchToAddStudentTeacherToCourse switchPanel) {
		this.rePackWindow = rePackWindow;
		this.switchPanel = switchPanel;
		tableModel.addColumn("Objective Column");
		tableModel.addColumn("Milestone 1: E");
		tableModel.addColumn("Milestone 1: C");
		tableModel.addColumn("Milestone 1: A");
		tableModel.addRow(new Object[] {});
		tableModel.addRow(new Object[] {});
		tableModel.setValueAt("Objective/Milestone",0,0);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		setUpButtons(rePackWindow, jMenuBar);

		setUpLayoutAndPanels();
	}

	private void setUpLayoutAndPanels() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints courseNameWriteConstraints = new GridBagConstraints();
		courseNameWriteConstraints.fill = GridBagConstraints.HORIZONTAL;
		courseNameWriteConstraints.insets = new Insets(0,0,0,0);
		courseNameWriteConstraints.gridx = 1;
		courseNameWriteConstraints.weightx = 1.0;

		GridBagConstraints courseObjectiveTableConstraints = new GridBagConstraints();
		courseObjectiveTableConstraints.fill = GridBagConstraints.BOTH;
		courseObjectiveTableConstraints.gridy = 1;
		courseObjectiveTableConstraints.weightx = 1.0;
		courseObjectiveTableConstraints.weighty = 1.0;
		courseObjectiveTableConstraints.insets = new Insets(0,0,0,0);

		GridBagConstraints courseNameTipFieldConstraints = new GridBagConstraints();
		courseNameTipFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		courseNameTipFieldConstraints.gridx = 0;

		GridBagConstraints courseNameContainerConstraints = new GridBagConstraints();
		courseNameContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
		courseNameContainerConstraints.insets = new Insets(0,0,0,0);
		courseNameContainerConstraints.weightx = 1.0;

		JPanel courseNameContainer = new JPanel(new GridBagLayout());
		JTextField courseNameTip = new JTextField();
		courseNameTip.setText("Course name :");
		courseNameTip.setEditable(false);
		courseNameContainer.add(courseNameTip, courseNameTipFieldConstraints);
		courseNameContainer.add(courseNameWriteField, courseNameWriteConstraints);

		pageHolder.setLayout(gridBagLayout);
		pageHolder.add(courseNameContainer,courseNameContainerConstraints);
		pageHolder.add(jScrollPane,courseObjectiveTableConstraints);
	}

	private void setUpButtons(final RePackWindow rePackWindow, JMenuBar jMenuBar) {
		addActivityButton.setAction(new AbstractAction()
		{
	    	@Override public void actionPerformed(final ActionEvent e) {
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}

				char[] gradeLevelArrayChar = new char[3];
				gradeLevelArrayChar[0] = 'E';
				gradeLevelArrayChar[1] = 'C';
				gradeLevelArrayChar[2] = 'A';
				int baseOfSet = (table.getColumnCount()-1)/3;
				for (int gradeLevel = 2; gradeLevel > -1; gradeLevel--) {
					table.addColumn(new TableColumn(1));
					table.getColumnModel().getColumn(table.getColumnCount()-1).setHeaderValue("Milestone "+
							(baseOfSet+1)+": "+gradeLevelArrayChar[gradeLevel]);
					table.moveColumn(table.getColumnCount()-1,(baseOfSet*(gradeLevel+1))+1);
				}
				rePackWindow.rePackWindow();

	    	}
		});
		addActivityButton.setText("Add Milestone");
		jMenuBar.add(addActivityButton);


		addGoalButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] {});
				rePackWindow.rePackWindow();
			}
		});
		addGoalButton.setText("Add Objective");
		jMenuBar.add(addGoalButton);

		continueToNextStep.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}

				String name = courseNameWriteField.getText();

				ArrayList<String> objectives = new ArrayList<>();
				ArrayList<String> milestone = new ArrayList<>();
				List<List<Integer>> maxPointModel = new ArrayList<>();

				if (table.getColumnCount() > 1 && table.getRowCount() > 1) {

					for (int col = 1; col < ((table.getColumnCount()-1)/3)+1; col++) {
						String arg = (String) table.getValueAt(0, col);
						if (arg != null && !arg.isEmpty()) {
							milestone.add(arg);
						}
					}

					for (int row = 1; row < table.getRowCount(); row++) {
						String arg = (String) table.getValueAt(row, 0);
						if (arg != null && !arg.isEmpty()) {
							objectives.add(arg);
						}
					}

					int baseOfSet = (table.getColumnCount()-1)/3;
					for (int row = 1; row < table.getRowCount(); row++) {
						maxPointModel.add(new ArrayList<>());
						for (int gradeLevel = 0; gradeLevel < 3; gradeLevel++) {
							for (int col = baseOfSet*gradeLevel+1; col < (baseOfSet*(gradeLevel))+1+milestone.size(); col++) {
								String arg = (String) table.getValueAt(row, col);
								if (arg != null && !arg.isEmpty()) {
									maxPointModel.get(row-1).add(Integer.parseInt(arg));
								}
								else {
									maxPointModel.get(row).add(0);
								}
							}
						}
					}


				}
				if (!milestone.isEmpty() && !objectives.isEmpty() && !name.isEmpty()) {
					clearMenuBar(jMenuBar);
					rePackWindow.rePackWindow();
					switchPanel.startChooseGroupPage(name, new CourseGoalModel(objectives, milestone, maxPointModel));
				}
				else {
					String newLine = System.getProperty("line.separator");
					JOptionPane.showMessageDialog(null,"At least one Objective has to be filled in and one milestone."+
							newLine+ "A name for the course have to be filled in.","Course creation",JOptionPane.INFORMATION_MESSAGE);
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

class courseJTable extends JTable {

	public courseJTable(TableModel dm) {
		super(dm);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (row == 0 && column == 0) {
			return false;
		}
		return super.isCellEditable(row, column);
	}
}

