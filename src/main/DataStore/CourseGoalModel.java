package main.DataStore;

import java.util.List;

/**
 * Created by phili on 2016-01-16.
 */
public class CourseGoalModel {

    private List<String> goals;
    private List<String> partGoals;

    public CourseGoalModel(List<String> goals, List<String> partGoals) {
        this.goals = goals;
        this.partGoals = partGoals;
    }

    public List<String> getGoals() {
        return goals;
    }

    public List<String> getPartGoals() {
        return partGoals;
    }
}
