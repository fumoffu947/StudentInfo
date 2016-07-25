package main.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2016-01-17.
 */
public class StudentGrade implements Serializable {

    // first row then columns
    private ArrayList<ArrayList<Integer>> grades = new ArrayList<>();

    public StudentGrade(ArrayList<ArrayList<Integer>> grades) {
        this.grades = grades;
    }

    public ArrayList<ArrayList<Integer>> getGrades() {
        return grades;
    }

    public boolean setValueAt(int row, int col, int value) {
        int oldValue = grades.get(row).get(col);
        grades.get(row).set(col,value);
        return oldValue == grades.get(row).get(col);
    }
}
