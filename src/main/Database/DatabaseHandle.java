package main.Database;

import main.DataStore.Student;
import main.DataStore.StudentGrade;
import main.DataStore.Teacher;
import main.Interfaces.Person;
import main.Interfaces.PersonSearchFunction;
import main.MainFrame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseHandle {

	private Logger logger = MainFrame.logger;

	private static DatabaseHandle databaseHandle = new DatabaseHandle();

	private static int MAX = 32672; // max int for the Java DB
	private static String[]
			tableConfig =  { "CREATE TABLE SI_PERSON_TABLE (PERSON_ID INTEGER GENERATED ALWAYS AS IDENTITY" +
					 " (START WITH 1, INCREMENT BY 1),FIRST_NAME varchar(64) NOT NULL, " +
					 "NAMES varchar(255) NOT NULL, SURNAME varchar(255) NOT NULL, CELL_NUM varchar(50)," +
					 "EMAIL varchar(255), IS_TEACHER BOOLEAN NOT NULL, PRIMARY KEY (PERSON_ID))",
			"CREATE TABLE SI_COURSE_TABLE (COURSE_ID INT NOT NULL  GENERATED ALWAYS AS IDENTITY, COURSE_NAME VARCHAR(1000) NOT NULL UNIQUE," +
			"GROUPS VARCHAR(200) NOT NULL, OTHER_STUDENTS VARCHAR(800) NOT NULL, TEACHERS VARCHAR(80) NOT NULL," +
			"OBJECTIVES VARCHAR("+MAX+") NOT NULL, MILESTONES VARCHAR("+MAX+") NOT NULL," +
			"MAX_POINTS VARCHAR("+MAX+") NOT NULL, REMOVED VARCHAR(800) NOT NULL, " +
			"GRADE_LEVELS VARCHAR("+MAX+") NOT NULL," +
			"PRIMARY KEY (COURSE_ID))",
			"CREATE TABLE SI_GRADE_TABLE (GRADE_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, STUDENT_ID INT NOT NULL," +
			"COURSE_ID INT NOT NULL, GRADE VARCHAR(6000) NOT NULL, PRIMARY KEY (GRADE_ID)," + // 20*2*20 = 2400 -> 3000(*2) TO BE SAFE 6000
			"FOREIGN KEY (STUDENT_ID) REFERENCES SI_PERSON_TABLE (PERSON_ID), " +
			"FOREIGN KEY (COURSE_ID) REFERENCES SI_COURSE_TABLE (COURSE_ID))",
			"CREATE TABLE SI_GROUP_TABLE (GROUP_ID INT NOT NULL  GENERATED ALWAYS AS IDENTITY, GROUP_NAME CARCHAR(500) NOT NULL UNIQUE," +
			" PERSONS VARCHAR(600) NOT NULL, PRIMARY KEY (GROUP_ID))"};
	private static String[] tablenames = {"SI_PERSON_TABLE","SI_GRADE_TABLE","SI_GROUP_TABLE","SI_COURSE_TABLE"};
	private String url = "jdbc:derby:content/SI_DATABASE;create=true";

	protected DatabaseHandle() {
		// wont be needed because the DriverManager.getConnetion() with derby inside will load the driver if not specified not to
		/*try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Class.forName("org.apache.derby.jdbc.ClientDriver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"Driver modules (files) were not found and could not load drivers for the database.");
		}*/

		// if username and password sould be used??

		CreateTables();
		shutdownDatabase();
	}

	public void shutdownDatabase() {
		try {
			DriverManager.getConnection("jdbc:derby:content/test;shutdown=true");
		} catch (SQLException e) {
			//e.printStackTrace();
			if (e instanceof SQLException) {
				String sqlState = ((SQLException) e).getSQLState();
				if (!sqlState.equalsIgnoreCase("XJ015")) { // check the right state and code for shuting down the DB

					System.err.println("SQLState: " +
							   ((SQLException)e).getSQLState());

					System.err.println("Error Code: " +
							   ((SQLException)e).getErrorCode());

					System.err.println("Message: " + e.getMessage());

					Throwable t = e.getCause();
					while(t != null) {
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
				} else {
					System.err.println("Database did not shutdown properly.");
				}
			}
		}

		// only if i load the drivers myself if ("org.apache.derby.jdbc.EmbeddedDriver".equals("org.apache.derby.jdbc.EmbeddedDriver")) {
		// should not need to do this because the JDK fixes it for me
		   /*boolean gotSQLExc = false;
		   try {
		      DriverManager.getConnection("jdbc:derby:;shutdown=true");
		   } catch (SQLException se)  {
		      if ( se.getSQLState().equals("XJ015") ) {
		         gotSQLExc = true;
		      }
		   }
		   if (!gotSQLExc) {
		      System.out.println("Database did not shut down normally");
		   }  else  {
		      System.out.println("Database shut down normally");
		   }*/
	}

	private void CreateTables() {
		try (Connection conn = DriverManager.getConnection(url)) {
			List<String> tmpExistingTables = new ArrayList<>();
			DatabaseMetaData md = conn.getMetaData();
			try (ResultSet rsmd = md.getTables(null, null, "SI_%", null)) {
				while (rsmd.next()) {
					final String table_name = rsmd.getString("TABLE_NAME");
					tmpExistingTables.add(table_name);
					System.out.println(table_name+" Already exists");
				}
			}
			for (int i = 0; i < tablenames.length; i++) {
				if (!tmpExistingTables.contains(tablenames[i])) {
					try (Statement stmt = conn.createStatement()) {
						stmt.executeUpdate(tableConfig[i]);
					}
				}
			}
			md = conn.getMetaData();
			System.out.println("These do now exist.");
			try (ResultSet rsmd = md.getTables(null, null, "SI_%", null)) {
				while (rsmd.next()) {
					final String table_name = rsmd.getString("TABLE_NAME");
					System.out.println(table_name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insertPerson(Person person) {
		try (Connection conn = DriverManager.getConnection(url)) {
			String insertQuery = "INSERT INTO SI_PERSON_TABLE (FIRST_NAME,NAMES,SURNAME,CELL_NUM,EMAIL,IS_TEACHER) " +
					     "VALUES (?,?,?,?,?,?)";
			String firstName;
			String names;
			String surname;
			String cellNum;
			String email;
			boolean isTeacher;
			if (person.isTeacher()) {
				Teacher teacher = (Teacher) person;
				firstName = teacher.getFirstName();
				names = teacher.getNameString();
				surname = teacher.getSurname();
				cellNum = teacher.getCellPhoneNumber();
				email = teacher.getEmailAddress();
				isTeacher = true;
			} else {
				Student student = (Student) person;
				firstName = student.getFirstName();
				names = student.getNameString();
				surname = student.getSurname();
				cellNum = student.getCellPhoneNumber();
				email = student.getEmailAddress();
				isTeacher = false;
			}
			PreparedStatement prepStmt = conn.prepareStatement(insertQuery);
			prepStmt.setString(1, firstName);
			prepStmt.setString(2, names);
			prepStmt.setString(3, surname);
			prepStmt.setString(4, cellNum);
			prepStmt.setString(5, email);
			prepStmt.setBoolean(6, isTeacher);
		}catch (SQLException e) {
			if (e instanceof SQLException) {
				printSQLException(e);
			}else {
				e.printStackTrace();
			}
			System.err.println("Could not create "+person+".");
			return false;
		}
		// get errors if there are any
		return true;
	}

	public void printSQLException(final SQLException e) {
		String sqlState = e.getSQLState();

		System.err.println("SQLState: " + e.getSQLState());

		System.err.println("Error Code: " + e.getErrorCode());

		System.err.println("Message: " + e.getMessage());

		Throwable t = e.getCause();
		while (t != null) {
			System.out.println("Cause: " + t);
			t = t.getCause();
		}
	}

	/**
	 * This method is used to check if the given prefix exists in a word in the PersonLexicon
	 * @param namePrefix
	 * the prefix to check after in the lexicon
	 * @return
	 * return true if the a name contains the prefix false if not
	 */ // chould fix so that prepStmt and rs is with try catch and finally so it always closes
	public boolean containsPrefix(String namePrefix) {
		boolean foundPrefix = false;
		try (Connection conn = DriverManager.getConnection(url)) {
			try (PreparedStatement prepStmt = conn.prepareStatement(
					"SELECT COUNT (ALL FIRST_NAME) AS count FROM SI_PERSON_TABLE WHERE FIRST_NAME LIKE ?")) {
				prepStmt.setString(1, namePrefix + '%');
				try (ResultSet rs = prepStmt.executeQuery()) {
					rs.next();
					int count = rs.getInt("count");
					if (count > 0) {
						foundPrefix = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return foundPrefix;
	}

	/**
	 * this method is meant to get the collection of persons with the given prefix
	 * in their name and return them
	 * @param namePrefix
	 * the given name to search after in the lexicon
	 * @return
	 * return ether a collection of persons with the given name
	 * or a empty collection if no persons by that name was found
	 */
	public Collection<Person> getPersonsWithPrefix(String namePrefix) {
		Collection<Person> collection = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(url)) {
			try (PreparedStatement prepStmt = conn.prepareStatement(
					"SELECT * FROM SI_PERSON_TABLE WHERE FIRST_NAME LIKE ?")) {
				prepStmt.setString(1, namePrefix + '%');
				try (ResultSet rs = prepStmt.executeQuery()) {
					while (rs.next()) {
						if (rs.getBoolean(7)) {
							List<String> names = Arrays.asList(rs.getString("NAMES").split(" "));
							collection.add(new Teacher(names,rs.getString("SURNAME"),
										   names.indexOf(rs.getString("FIRST_NAME")),rs.getString("CELL_NUM"),
										   rs.getString("EMAIL"),rs.getInt("PERSON_ID")));
						} else {
							List<String> names = Arrays.asList(rs.getString("NAMES").split(" "));
							collection.add(new Teacher(names,rs.getString("SURNAME"),
										   names.indexOf(rs.getString("FIRST_NAME")),rs.getString("CELL_NUM"),
										   rs.getString("EMAIL"),rs.getInt("PERSON_ID")));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return collection;
	}

	/**
	 * this method is meant to get the collection of persons with the given name
	 * and return them
	 * @param name
	 * the given name to search after in the lexicon
	 * @return
	 * return ether a collection of persons with the given name
	 * or a empty collection if no persons by that name was found
	 */
	public Collection<Person> getPersons(String name, boolean isTeacher) {
		Collection<Person> collection = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(url)) {
			try (PreparedStatement prepStmt = conn.prepareStatement(
					"SELECT * FROM SI_PERSON_TABLE WHERE FIRST_NAME = ? AND IS_TEACHER = ?")) {
				prepStmt.setString(1, name);
				prepStmt.setBoolean(2,isTeacher);
				try (ResultSet rs = prepStmt.executeQuery()) {
					while (rs.next()) {
						if (rs.getBoolean(7)) {
							List<String> names = Arrays.asList(rs.getString("NAMES").split(" "));
							collection.add(new Teacher(names,rs.getString("SURNAME"),
										   names.indexOf(rs.getString("FIRST_NAME")),rs.getString("CELL_NUM"),
										   rs.getString("EMAIL"),rs.getInt("PERSON_ID")));
						} else {
							List<String> names = Arrays.asList(rs.getString("NAMES").split(" "));
							collection.add(new Teacher(names,rs.getString("SURNAME"),
										   names.indexOf(rs.getString("FIRST_NAME")),rs.getString("CELL_NUM"),
										   rs.getString("EMAIL"),rs.getInt("PERSON_ID")));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return collection;
	}

	/**
	 * @param personID
	 * the personID to get the grade for and follow the name from the person
	 * @param courseID
	 * the course to get the grade for
	 * @return
	 * returns A CourseGrade if person has one else return null
	 */
	public StudentGrade getCourseGradeByPerson(int personID, int courseID) {
		StudentGrade grade = null;
		try (Connection conn = DriverManager.getConnection(url)) {
			try (PreparedStatement prepStmt = conn.prepareStatement(
					"SELECT * FROM SI_GRADE_TABLE WHERE " + "STUDENT_ID = ? AND COURSE_ID = ?")) {
				prepStmt.setInt(1, personID);
				prepStmt.setInt(2, courseID);
				try (ResultSet rs = prepStmt.executeQuery()) {
					rs.next();
					byte[] bytes = rs.getBytes("GRADE");
					ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
					try (ObjectInputStream ois = new ObjectInputStream(bais)) {
						grade = (StudentGrade) ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		} catch (SQLException e) {
			printSQLException(e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return grade;
	}

	/**
	 * @param studentID
	 * the student to add the grade to or change the grade
	 * @param studentGrade
	 * the grade that will be inserted or changed to
	 * @param courseID
	 * the name of the course that the grade will be added to
	 * @return
	 * return true if the the grade was inserted else false if it failed
	 */
	public boolean insertStudentGrade(int studentID, StudentGrade studentGrade, int courseID) {
		try (Connection conn = DriverManager.getConnection(url)) {
			PreparedStatement prepStmt = conn
		} catch(SQLException e) {
			printSQLException(e);
			return false;
		}
		return false;
	}

	/**
	 * Removes the given person from the lexicon and all grades associated with that person
	 * @param person
	 * given person to remove from the lexicon
	 * @return
	 * return true if the person exists and was removed else false
	 */
	public boolean removePerson(Person person) {
		return false;
	}

	/**
	 * This is meant to get all the students in the lexicon
	 * it goes through all the nodes and add all the persons who
	 * is a student to a list and then return the list to the caller
	 * @return
	 * A list of all the student currently in the lexicon
	 */
	public List<Student> getAllStudents() {
		return null;
	}

	/**
	 * this method returns all the course names for the given person
	 * @param person
	 * Given person to get the courses for
	 * @return
	 * returns a list of all the courses for given person
	 */
	public List<String> getCourseNamesForPerson(Person person) {
		return null;
	}

	public void removeCourseFromPersons(List<Student> students, String courseName) {

	}

	public static DatabaseHandle getDatabaseHandle() {
		return databaseHandle;
	}
}
