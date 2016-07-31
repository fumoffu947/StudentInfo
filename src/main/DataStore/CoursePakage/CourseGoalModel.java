package main.DataStore.CoursePakage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by phili on 2016-01-16.
 */
public class CourseGoalModel {

    private final List<List<Integer>> maxPoints;
    private List<String> objective;
    private List<String> milestone;

    public CourseGoalModel(List<String> objective, List<String> milestone, List<List<Integer>> maxPoints) {
        this.objective = objective;
        this.milestone = milestone;
        this.maxPoints = maxPoints;
    }

    public List<List<Integer>> getMaxPoints() {
        return maxPoints;
    }

    public List<String> getObjective() {
        return objective;
    }

    public List<String> getMilestone() {
        return milestone;
    }

    public CourseGoalModel getClone() {
        List<List<Integer>> maxPointsNew = new ArrayList<>();
        List<String> objectiveNew = new ArrayList<>();
        List<String> milestoneNew = new ArrayList<>();

        for (int row = 0; row < maxPoints.size(); row++) {
            maxPointsNew.add(new ArrayList<>());
            for (int col = 0; col < maxPoints.get(row).size(); col++) {
                maxPointsNew.get(row).add(maxPoints.get(row).get(col));
            }
        }
        objectiveNew.addAll(objective.stream().collect(Collectors.toList()));
        milestoneNew.addAll(milestone.stream().collect(Collectors.toList()));

        return new CourseGoalModel(objectiveNew,milestoneNew,maxPointsNew);
    }
}
