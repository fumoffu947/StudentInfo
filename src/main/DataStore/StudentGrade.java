package main.DataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2016-01-17.
 */
public class StudentGrade {

    // first row then columns
    private ArrayList<ArrayList<Integer>> grades = new ArrayList<>();

    public StudentGrade(ArrayList<ArrayList<Integer>> grades) {
        this.grades = grades;
    }

    public ArrayList<ArrayList<Integer>> getGrades() {
        return grades;
    }
}
