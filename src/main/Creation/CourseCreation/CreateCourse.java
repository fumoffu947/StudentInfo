package main.Creation.CourseCreation;

import main.DataStore.CourseGoalModel;
import main.DataStore.CourseGradeModel;
import main.DataStore.ShowPages.CoursesPage;
import main.Interfaces.*;
import main.Interfaces.PaneInterfaceSwitches.SwitchToAddStudentTeacherToCourse;
import main.Interfaces.PaneInterfaceSwitches.SwitchToCreateFromCourse;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class CreateCourse implements main.Interfaces.Panel {

	private final RePackWindow rePackWindow;
	private final SwitchToAddStudentTeacherToCourse switchPanel;
	private final SwitchToCreateFromCourse switchToCreateFromCourse;
	private final CoursesPage coursePage;
	private JTextField courseNameWriteField = new JTextField();
	private JPanel pageHolder = new JPanel();

	private DefaultTableModel tableModel = new DefaultTableModel();
	private courseJTable table = new courseJTable(tableModel);

	private  DefaultTableModel gradeLevelTableModel = new DefaultTableModel();
	private JTable gradeLevelTable = new JTable(gradeLevelTableModel);

	private JScrollPane jScrollPane = new JScrollPane(table);

	private JButton addMilestoneButton = new JButton();
	private JButton addObjectiveButton = new JButton();
	private JButton continueToNextStep = new JButton();
	private JButton createFromCourseButton = new JButton();

	private int nextObjectiveNumber = 2;

	private Pattern numberPattern = Pattern.compile("[0-9]+");

	public CreateCourse(RePackWindow rePackWindow, SwitchToAddStudentTeacherToCourse switchPanel, SwitchToCreateFromCourse switchToCreateFromCourse,
						CoursesPage coursesPage) {
		this.rePackWindow = rePackWindow;
		this.switchPanel = switchPanel;
		this.coursePage = coursesPage;
		this.switchToCreateFromCourse = switchToCreateFromCourse;
		tableModel.addColumn("Objective Column");
		tableModel.addColumn("Milestone 1: E");
		tableModel.addColumn("Milestone 1: C");
		tableModel.addColumn("Milestone 1: A");
		tableModel.addRow(new Object[] {});
		tableModel.addRow(new Object[] {});
		tableModel.setValueAt("Objective/Milestone",0,0);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		gradeLevelTableModel.addColumn("Objective Column");
		gradeLevelTableModel.addColumn("Grade: E");
		gradeLevelTableModel.addColumn("Grade: C");
		gradeLevelTableModel.addColumn("Grade: A");
		gradeLevelTableModel.addRow(new Object[] {});
		gradeLevelTableModel.addRow(new Object[] {"Objective 1","0","0","0"});

		// setup modelListener so that when user changes value at milestone n all milestone n are changed
		TableModelListener tableModelListener = new TableModelListener()
		{
			@Override public void tableChanged(final TableModelEvent e) {
				tableModel.removeTableModelListener(this);
				System.out.println("tabel model changed");
				System.out.println("selected row: "+ table.getSelectedRow()+" selected column: "+table.getSelectedColumn());
				if (table.getSelectedRow() == 0) {
					String valueToCopy = (String) table.getValueAt(table.getSelectedRow(),table.getSelectedColumn());
					if (valueToCopy != null) {
						int objectiveNum = (table.getColumnCount()-1)/3;
						int mileOffset = ((table.getSelectedColumn() - 1) % objectiveNum);
						for (int gradeIndex = 0; gradeIndex < 3; gradeIndex++) {
							table.setValueAt(valueToCopy,0,mileOffset+(objectiveNum*gradeIndex)+1);
						}
					}

				}
				if (table.getSelectedColumn() > 0 && table.getSelectedRow() > 0) {
					if (!numberPattern.matcher((String) table.getValueAt(table.getSelectedRow(),table.getSelectedColumn())).matches()) {
						String newLine = System.getProperty("line.separator");
						JOptionPane.showMessageDialog(null,"You can only write numbers inside of the table"+newLine+"thus outside of the objective column and milestone row");
						tableModel.setValueAt("",table.getSelectedRow(), table.getSelectedColumn());
					}
				}
				tableModel.addTableModelListener(this);
			}
		};
		table.getModel().addTableModelListener(tableModelListener);

		TableModelListener gradeModelListener = new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				gradeLevelTableModel.removeTableModelListener(this);
				int selectedRow = gradeLevelTable.getSelectedRow();
				int selectedColumn = gradeLevelTable.getSelectedColumn();
				if (selectedRow != -1 && selectedColumn != -1) {
					String newValue = (String) gradeLevelTable.getValueAt(selectedRow, selectedColumn);
					if (!numberPattern.matcher(newValue).matches()) {
						JOptionPane.showMessageDialog(null, "You can only write numbers in the this table.", "Not A Number", JOptionPane.ERROR_MESSAGE);
						gradeLevelTableModel.setValueAt(0, selectedRow, selectedColumn);
					}
				}
				gradeLevelTableModel.addTableModelListener(this);
			}
		};
		gradeLevelTableModel.addTableModelListener(gradeModelListener);

		setUpLayoutAndPanels();

		table.setAutoCreateColumnsFromModel(false);
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
		courseObjectiveTableConstraints.gridx = 0;
		courseObjectiveTableConstraints.gridy = 1;
		courseObjectiveTableConstraints.weightx = 4.0;
		courseObjectiveTableConstraints.weighty = 1.0;
		courseObjectiveTableConstraints.insets = new Insets(0,0,0,0);

		GridBagConstraints courseNameTipFieldConstraints = new GridBagConstraints();
		courseNameTipFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		courseNameTipFieldConstraints.gridx = 0;

		GridBagConstraints courseNameContainerConstraints = new GridBagConstraints();
		courseNameContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
		courseNameContainerConstraints.insets = new Insets(0,0,0,0);
		courseNameContainerConstraints.weightx = 1.0;
		courseNameContainerConstraints.gridwidth = 2;

		GridBagConstraints gradeLevelTableConstraints = new GridBagConstraints();
		gradeLevelTableConstraints.gridx = 1;
		gradeLevelTableConstraints.gridy = 1;
		gradeLevelTableConstraints.weightx = 0.5;
		gradeLevelTableConstraints.weighty = 1.0;
		gradeLevelTableConstraints.fill = GridBagConstraints.BOTH;

		JPanel courseNameContainer = new JPanel(new GridBagLayout());
		JTextField courseNameTip = new JTextField();
		courseNameTip.setText("Course name :");
		courseNameTip.setEditable(false);
		courseNameContainer.add(courseNameTip, courseNameTipFieldConstraints);
		courseNameContainer.add(courseNameWriteField, courseNameWriteConstraints);

		pageHolder.setLayout(gridBagLayout);
		pageHolder.add(courseNameContainer,courseNameContainerConstraints);
		pageHolder.add(jScrollPane,courseObjectiveTableConstraints);
		pageHolder.add(new JScrollPane(gradeLevelTable), gradeLevelTableConstraints);
	}

	private void setUpButtons(final RePackWindow rePackWindow, JMenuBar jMenuBar) {
		addMilestoneButton.setAction(new AbstractAction()
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
					tableModel.addColumn("Milestone");
					table.addColumn(new TableColumn(tableModel.getColumnCount()-1));
					table.getColumnModel().getColumn(table.getColumnCount()-1).setHeaderValue("Milestone "+
														  (baseOfSet+1)+": "+gradeLevelArrayChar[gradeLevel]);
					table.moveColumn(table.getColumnCount()-1,(baseOfSet*(gradeLevel+1))+1);
				}
				rePackWindow.rePackWindow();

			}
		});
		addMilestoneButton.setText("Add Milestone");
		jMenuBar.add(addMilestoneButton);


		addObjectiveButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] {});
				gradeLevelTableModel.addRow(new Object[] {"Objective "+nextObjectiveNumber,"0","0","0"});
				nextObjectiveNumber++;
				rePackWindow.rePackWindow();
			}
		});
		addObjectiveButton.setText("Add Objective");
		jMenuBar.add(addObjectiveButton);

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
				List<List<Integer>> gradeLevelModel = new ArrayList<>();

				if (table.getColumnCount() > 1 && table.getRowCount() > 1) {

					// ad all the milestone to a list and save their postions to make the extraction of
					// maxPoints easier
					ArrayList<Integer> milestonePositions = new ArrayList<>();
					for (int col = 1; col < table.getColumnCount(); col++) {
						String arg = (String) table.getValueAt(0, col);
						if (arg != null && !arg.isEmpty()) {
							if (col < ((table.getColumnCount()-1)/3)+1) {
								milestone.add(arg);
							}
							milestonePositions.add(col);
						}
					}

					// add all the objectives
					ArrayList<Integer> objectivePositions = new ArrayList<Integer>();
					for (int row = 1; row < table.getRowCount(); row++) {
						String arg = (String) table.getValueAt(row, 0);
						if (arg != null && !arg.isEmpty()) {
							objectives.add(arg);
							objectivePositions.add(row);
						}
					}

					// add the max points for each milestone
					for (int row = 1; row < table.getRowCount(); row++) {
						maxPointModel.add(new ArrayList<>());
						for (int col = 0; col < milestonePositions.size(); col++) {
							String arg = (String) table.getValueAt(row, milestonePositions.get(col));
							if (arg != null && !arg.isEmpty()) {
								maxPointModel.get(row-1).add(Integer.parseInt(arg));
							}
							else {
								maxPointModel.get(row-1).add(0);
							}
						}

					}

					// add the gradeLevel values for each objective
					for (int row = 0; row < objectivePositions.size(); row++) {
						gradeLevelModel.add(new ArrayList<>());
						for (int col = 1; col < 4; col++) {
							String arg = (String) gradeLevelTable.getValueAt(objectivePositions.get(row), col);
							if (arg != null && !arg.isEmpty()) {
								gradeLevelModel.get(row).add(Integer.parseInt(arg));
							}else {
								gradeLevelModel.get(row).add(0);
							}
						}
					}
				}
				if (!milestone.isEmpty() && !objectives.isEmpty() && name != null && !name.isEmpty() && !coursePage.containsCourseWithName(name)) {
					clearMenuBar(jMenuBar);
					rePackWindow.rePackWindow();
					switchPanel.startChooseGroupPage(name, new CourseGoalModel(objectives, milestone, maxPointModel), new CourseGradeModel(gradeLevelModel));
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

		createFromCourseButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToCreateFromCourse.switchToPage();
			}
		});
		createFromCourseButton.setText("Create From Course");
		jMenuBar.add(createFromCourseButton);
	}

	@Override
	public void clearMenuBar(JMenuBar jMenuBar) {
		jMenuBar.remove(addMilestoneButton);
		jMenuBar.remove(addObjectiveButton);
		jMenuBar.remove(continueToNextStep);
		jMenuBar.remove(createFromCourseButton);
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

