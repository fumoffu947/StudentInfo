package main.DataStore.Lexicon;

import main.DataStore.Student;
import main.DataStore.StudentGrade;
import main.DataStore.Teacher;
import main.Interfaces.Person;
import main.Interfaces.PersonSearchFunction;

import java.io.*;
import java.util.*;

/**
 * Created by philip on 2016-01-13.
 */
public class PersonLexicon {

    private LexiconNode startNode = new LexiconNode();

    public PersonLexicon() {
    }

    public PersonLexicon(BufferedInputStream fileReader) {
        // read in the lexicon
        try {
            int character = fileReader.read();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while(character != -1) {
                if ('{' == character) {
                    if (byteArrayOutputStream.size() != 0) {
                        String personString = new String(Base64.getDecoder().decode(byteArrayOutputStream.toByteArray()));
                        loadPersonFromString(personString.split(";"));
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
    }

    private void loadPersonFromString(String[] personArray) {
        //[names,surnames,firstname,cellnum,email,ID]
        String[] studentArray = personArray[0].split(",");

        Student student = new Student(Arrays.asList(studentArray[0].split(" ")), studentArray[1], Integer.parseInt(studentArray[2]), studentArray[3], studentArray[4], Integer.parseInt(studentArray[5]));
        insertPersonToLexicon(student.getFirstName(),student);

        for (int courseIndex = 1; courseIndex < personArray.length; courseIndex++) {
            String[] currentCourse = personArray[courseIndex].split(":");
            String[] courseInfo = currentCourse[0].split(",");
            String[] gradeArray = currentCourse[1].split(",");
            int gradeRows = Integer.parseInt(courseInfo[1]);
            int gradeCols = Integer.parseInt(courseInfo[2]);
            ArrayList<ArrayList<Integer>> grade = new ArrayList<>();
            for (int row = 0; row < gradeRows; row++) {
                grade.add(new ArrayList<>());
                for (int coll = 0; coll < gradeCols; coll++) {
                    grade.get(row).add(Integer.parseInt(gradeArray[row*gradeCols+coll]));
                }
            }
            insertStudentGrade(student,new StudentGrade(grade),courseInfo[0]);
        }
    }

    public void saveLexicon(BufferedOutputStream outputStream) {
        recursiveSave(outputStream, startNode);
        try {
            outputStream.write("{".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recursiveSave(BufferedOutputStream outputStream, LexiconNode node) {
        if (!node.getNamesMap().isEmpty()) {
            Set<String> keySet = node.getNamesMap().keySet();
            for (String personName : keySet) {
                Collection<Person> personCollection = node.getNamesMap().get(personName);

                for (Person person : personCollection) {

                    try {
                        // start of person
                        outputStream.write("{".getBytes());



                        StringBuilder stringBuilder = new StringBuilder();
                        HashMap<String,StudentGrade> gradesMap = null;

                        if (node.getGrades().containsKey(person.getID())) {
                            gradesMap = node.getGrades().get(person.getID());
                        }
                        if (person.isTeacher()) {
                            Teacher teacher = (Teacher) person;

                            // add the name for the teacher
                            for (int nameIndex = 0; nameIndex < teacher.getName().size(); nameIndex++) {
                                stringBuilder.append(teacher.getName().get(nameIndex));
                                if (nameIndex != teacher.getName().size()-1) {
                                    stringBuilder.append(" ");
                                }
                            }
                            stringBuilder.append(",");

                            // add surname
                            stringBuilder.append(teacher.getLastName());
                            stringBuilder.append(",");

                            //add the position of the firstname
                            stringBuilder.append(teacher.getFirstNameNumber());
                            stringBuilder.append(",");

                            // add the cellnumber
                            stringBuilder.append(teacher.getCellPhoneNumber());
                            stringBuilder.append(",");

                            // add the email
                            stringBuilder.append(teacher.getEmailAddress());
                            stringBuilder.append(",");

                            // add the ID
                            stringBuilder.append(teacher.getID());
                            stringBuilder.append(";");

                            // write the encoded person
                            outputStream.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
                            stringBuilder.delete(0, stringBuilder.length()-1);

                        }else {
                            Student student = (Student) person;

                            // add the name for the Student
                            for (int nameIndex = 0; nameIndex < student.getName().size(); nameIndex++) {
                                stringBuilder.append(student.getName().get(nameIndex));
                                if (nameIndex != student.getName().size()-1) {
                                    stringBuilder.append(" ");
                                }
                            }
                            stringBuilder.append(",");

                            // add surname
                            stringBuilder.append(student.getLastName());
                            stringBuilder.append(",");

                            //add the position of the firstname
                            stringBuilder.append(student.getFirstNameNumber());
                            stringBuilder.append(",");

                            // add the cellnumber
                            stringBuilder.append(student.getCellPhoneNumber());
                            stringBuilder.append(",");

                            // add the email
                            stringBuilder.append(student.getEmailAddress());
                            stringBuilder.append(",");

                            // add the ID
                            stringBuilder.append(student.getID());
                            // end of current person before courses
                            stringBuilder.append(";");

                            if (gradesMap != null) {
                                Set<String> coursesKey = gradesMap.keySet();

                                for (String courseName : coursesKey) {
                                    StudentGrade currentGrade = gradesMap.get(courseName);

                                    // add the name and the number of row and coll int the student grade
                                    stringBuilder.append(courseName);
                                    stringBuilder.append(",");

                                    // rows
                                    stringBuilder.append(currentGrade.getGrades().size());
                                    stringBuilder.append(",");

                                    // columns
                                    stringBuilder.append(currentGrade.getGrades().get(0).size());
                                    // end of grade info , after comes the actual grades
                                    stringBuilder.append(":");

                                    for (int row = 0; row < currentGrade.getGrades().size(); row++) {
                                        for (int col = 0; col < currentGrade.getGrades().get(row).size(); col++) {
                                            stringBuilder.append(currentGrade.getGrades().get(row).get(col));
                                            if (row == currentGrade.getGrades().size()-1 && col == currentGrade.getGrades().get(row).size()-1) {
                                                continue;
                                            }else {
                                                stringBuilder.append(",");
                                            }
                                        }
                                    }

                                    // end of course info
                                    stringBuilder.append(";");
                                }
                            }
                            // write the encoded person
                            outputStream.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
                            stringBuilder.delete(0, stringBuilder.length()-1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!node.getChildren().isEmpty()) {
            Set<Character> childrenKeys = node.getChildren().keySet();
            for (Character childeKey : childrenKeys) {
                recursiveSave(outputStream,node.getChildren().get(childeKey));
            }
        }
    }

    /**
     *
     * @param name
     * the name that will lead to the person in the lexicon
     * @param person
     * the person to store at the end point in the lexicon
     * @return
     * returns true if person was inserted
     */
    public boolean insertPersonToLexicon(String name, Person person) {
        LexiconNode currentNode = startNode;
        int namePosition = 0;
        while (namePosition < name.length() && currentNode.containsChildWithChar(name.charAt(namePosition))) {
            currentNode = currentNode.getChildWithChar(name.charAt(namePosition));
            namePosition++;
        }
        while(namePosition < name.length()) {
            LexiconNode tmp = new LexiconNode();
            currentNode.insertChild(name.charAt(namePosition), tmp);
            currentNode = tmp;
            namePosition++;
        }
        return currentNode.insertPersonToNode(name, person);
    }

    /**
     * This method is used to check if the given prefix exists in a word in the PersonLexicon
     * @param prefix
     * the prefix to check after in the lexicon
     * @return
     * return true if the a name contains the prefix false if not
     */
    public boolean containsPrefix(String prefix) {
        LexiconNode currentNode = startNode;
        int prefixPos = 0;
        while (prefixPos < prefix.length() && currentNode.containsChildWithChar(prefix.charAt(prefixPos))) {
            currentNode = currentNode.getChildWithChar(prefix.charAt(prefixPos));
            prefixPos++;
        }
        return prefixPos == prefix.length();
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
    private Collection<Person> getPersons(String name, PersonSearchFunction func) {
        Collection<Person> persons = new ArrayList<>();
        LexiconNode currentNode = startNode;
        int namePos = 0;
        while (namePos < name.length() && currentNode.containsChildWithChar(name.charAt(namePos))) {
            currentNode = currentNode.getChildWithChar(name.charAt(namePos));
            namePos++;
        }
        if (namePos == name.length() && currentNode.containsName(name)) {
            Collection<Person> personsTmp = currentNode.getPersonsWithName(name);
            for (Person person: personsTmp) {
                if (func.personCriteria(person)) {
                    persons.add(person);
                }
            }
        }
        return persons;
    }

    public Collection<Person> getPersonByNameAndFunction(String name, PersonSearchFunction func) {
        return getPersons(name, func);
    }

    public Collection<Person> getPersonsByName(String name) {
        return getPersons(name, (Person p)->true);
    }

    /**
     * @param person
     * the person to get the grade for and follow the name from the person
     * @param courseName
     * the course to get the grade for
     * @return
     * returns A CourseGrade if person has one else return null
     */
    public StudentGrade getCourseGradeByPerson(Person person, String courseName) {
        LexiconNode currentNode = startNode;
        int namePos = 0;
        while (namePos < person.getFirstName().length() && currentNode.containsChildWithChar(person.getFirstName().charAt(namePos))) {
            currentNode = currentNode.getChildWithChar(person.getFirstName().charAt(namePos));
            namePos++;
        }

        if (namePos == person.getFirstName().length()) {
            return currentNode.getGradeByPersonCourse(person, courseName);
        }
        return null;
    }

    /**
     * @param student
     * the students to add the grade to or change the grade
     * @param studentGrade
     * the grade that will be inserted or changed to
     * @param courseName
     * the name of the course that the grade will be added to
     * @return
     * return true if the the grade was inserted else false if it failed
     */
    public boolean insertStudentGrade(Student student, StudentGrade studentGrade, String courseName) {
        LexiconNode currentNode = startNode;
        int namePos = 0;
        while (namePos < student.getFirstName().length() && currentNode.containsChildWithChar(student.getFirstName().charAt(namePos))) {
            currentNode = currentNode.getChildWithChar(student.getFirstName().charAt(namePos));
            namePos++;
        }
        if (namePos == student.getFirstName().length()) {
            return currentNode.insertStudentGradeToCourseMap(student,studentGrade,courseName);
        }
        return false;
    }
}
