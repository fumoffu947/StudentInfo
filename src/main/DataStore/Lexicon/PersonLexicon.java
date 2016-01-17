package main.DataStore.Lexicon;

import main.DataStore.Student;
import main.DataStore.StudentGrade;
import main.Interfaces.Person;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by philip on 2016-01-13.
 */
public class PersonLexicon {

    private LexiconNode startNode = new LexiconNode();

    public PersonLexicon() {
        // read in the lexicon
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
    public Collection<Person> getPersonsByName(String name) {
        Collection<Person> persons = new ArrayList<>();
        LexiconNode currentNode = startNode;
        int namePos = 0;
        while (namePos < name.length() && currentNode.containsChildWithChar(name.charAt(namePos))) {
            currentNode = currentNode.getChildWithChar(name.charAt(namePos));
            namePos++;
        }
        if (namePos == name.length() && currentNode.containsName(name)) {
            persons = currentNode.getPersonsWithName(name);
        }
        return persons;
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
