package main.DataStore;

import java.util.List;

/**
 * Created by phili on 2016-01-16.
 */
public class CourseGoalModel {

    private final List<List<Integer>> maxPoits;
    private List<String> objective;
    private List<String> milestone;

    public CourseGoalModel(List<String> objective, List<String> milestone, List<List<Integer>> maxPoints) {
        this.objective = objective;
        this.milestone = milestone;
        this.maxPoits = maxPoints;
    }

    public List<List<Integer>> getMaxPoits() {
        return maxPoits;
    }

    public List<String> getObjective() {
        return objective;
    }

    public List<String> getMilestone() {
        return milestone;
    }
}
