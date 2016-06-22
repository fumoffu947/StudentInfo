package main.DataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2016-03-30.
 */
public class CourseGradeModel {

    private List<List<Integer>> gradeLevels;

    public CourseGradeModel(List<List<Integer>> gradeLevels) {
        this.gradeLevels = gradeLevels;
    }

    public List<Integer> getObjetiveGradeLevel(int objectivePos) {
        return gradeLevels.get(objectivePos);
    }

    public boolean setNewObjectiveLevelValue(int objectivePos, int levelGrade, int levelValue) {
        try {
            gradeLevels.get(objectivePos).set(levelGrade, levelValue);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public List<List<Integer>> getGradeLevels() {
        return gradeLevels;
    }

    public CourseGradeModel getClone() {
        List<List<Integer>> gradeLevelsNew = new ArrayList<>();

        for (int row = 0; row < gradeLevels.size(); row++) {
            gradeLevelsNew.add(new ArrayList<>());
            for (int col = 0; col < gradeLevels.get(row).size(); col++) {
                gradeLevelsNew.get(row).add(gradeLevels.get(row).get(col));
            }
        }

        return new CourseGradeModel(gradeLevelsNew);
    }
}
