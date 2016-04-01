package main.DataStore.Lexicon;

import main.DataStore.StudentGrade;
import main.Interfaces.Person;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by philip on 2016-01-13.
 */
public class LexiconNode {

    private HashMap<Character, LexiconNode> children = new HashMap<>();
    private  HashMap<String, Collection<Person>> namesMap = new HashMap<>();
    private HashMap<Integer, HashMap<String,StudentGrade>> grades = new HashMap<>();

    public LexiconNode() {
    }

    public boolean containsChildWithChar(Character character) {
        return children.containsKey(character);
    }

    public LexiconNode getChildWithChar(Character character) {
        return children.get(character);
    }

    public boolean containsName(String name) {
        return namesMap.containsKey(name);
    }

    public Collection<Person> getPersonsWithName(String name) {
        return namesMap.get(name);
    }

    public boolean insertChild(Character character , LexiconNode node) {
        if (!children.containsKey(character)) {
            children.put(character, node);
            return true;
        }
        return false;
    }

    public boolean insertPersonToNode(String name, Person person) {
        if (!namesMap.containsKey(name)) {
            Collection<Person> collection = new ArrayList<>();
            collection.add(person);
            namesMap.put(name, collection);
            return true;

        }else {
            Collection<Person> collection = namesMap.get(name);
            for (Person p : collection) {
                if (person.getID() == p.getID()) {
                    return false;
                }
            }
            collection.add(person);
            return true;
        }
    }

    public boolean insertStudentGradeToCourseMap(Person person, StudentGrade studentGrade, String courseName) {
        if (!grades.containsKey(person.getID())) {
            HashMap<String, StudentGrade> map = new HashMap<>();
            map.put(courseName, studentGrade);
            grades.put(person.getID(), map);
        } else {
            grades.get(person.getID()).put(courseName, studentGrade);
        }
        return grades.get(person.getID()).get(courseName).getGrades().equals(studentGrade.getGrades());
    }

    public  StudentGrade getGradeByPersonCourse(Person person, String courseName) {
        return grades.get(person.getID()).get(courseName);
    }

    public List<String> getCourseNamesByID(int ID) {
        List<String> names = new ArrayList<>();
        HashMap<String, StudentGrade> studentGradesMap = grades.get(ID);
        if (studentGradesMap != null) {
            names.addAll(studentGradesMap.keySet().stream().collect(Collectors.toList()));
        }
        return names;
    }

    public HashMap<Character, LexiconNode> getChildren() {
        return children;
    }

    public HashMap<String, Collection<Person>> getNamesMap() {
        return namesMap;
    }

    public HashMap<Integer, HashMap<String, StudentGrade>> getGrades() {
        return grades;
    }
}
