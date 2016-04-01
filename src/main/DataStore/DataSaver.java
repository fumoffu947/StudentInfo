package main.DataStore;

import main.DataStore.Lexicon.PersonLexicon;
import main.DataStore.ShowPages.CoursesPage;
import main.DataStore.ShowPages.YearHolderPage;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * Created by phili on 2016-02-14.
 */
public class DataSaver implements Runnable {

    private final PersonLexicon personLexicon;
    private final YearHolderPage yearHolderPage;
    private final CoursesPage coursesPage;
    private final SettingsLoader settingsLoader;

    public DataSaver(PersonLexicon personLexicon, YearHolderPage yearHolderPage, CoursesPage coursesPage, SettingsLoader settingsLoader) {
        this.personLexicon = personLexicon;
        this.yearHolderPage = yearHolderPage;
        this.coursesPage = coursesPage;
        this.settingsLoader = settingsLoader;
    }

    @Override
    public void run() {
        System.out.println("started saving");

        try {
            // saving the persons and grade for the students
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("content/Students.txt"));
            personLexicon.saveLexicon(fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // save the groups of people
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("content/Groups.txt"));

            // save each group of people
            List<ClassInfo> groups = yearHolderPage.getClasses();
            for (int index = 0; index < groups.size(); index++) {
                //start of group
                fileWriter.write("[".getBytes());

                // a stringBuilder for each group and then write the group to the file and end with ":" if not the last then end with ";"
                StringBuilder stringBuilder = new StringBuilder();

                // the group name
                stringBuilder.append(groups.get(index).getClassName());
                stringBuilder.append(",");

                // adding all the persons by firstName and ID with "," as separator between persons and "space" between name and ID
                for (int studentIndex = 0; studentIndex < groups.get(index).getStudents().size(); studentIndex++) {
                    stringBuilder.append(groups.get(index).getStudents().get(studentIndex).getFirstName());
                    stringBuilder.append(" ");
                    stringBuilder.append(groups.get(index).getStudents().get(studentIndex).getID());
                    if (studentIndex != groups.get(index).getStudents().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                fileWriter.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
            }
            fileWriter.write("[".getBytes());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // saves the courses
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("content/Courses.txt"));
            // save courses
            List<CourseInfo> courses = coursesPage.getCourses();
            for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++) {
                StringBuilder stringBuilder = new StringBuilder();

                // start of courseInfo
                fileWriter.write("<".getBytes());

                // add the name for the course
                stringBuilder.append(courses.get(courseIndex).getCourseName());
                // end of info 1
                stringBuilder.append(";");

                // writhe the name for the class to add to the course to get from the YearHolderPage (May change name to Groups or similar)
                for (int i = 0; i < courses.get(courseIndex).getClassInfoList().size(); i++) {
                    stringBuilder.append(courses.get(courseIndex).getClassInfoList().get(i).getClassName());
                    if (i != courses.get(courseIndex).getClassInfoList().size()-1) {
                        stringBuilder.append(",");
                    }
                }

                // end of info 2
                stringBuilder.append(";");

                // writhe the other enlisted students to the course
                for (int otherIndex = 0; otherIndex < courses.get(courseIndex).getOtherEnlistedStudents().size(); otherIndex++) {
                    stringBuilder.append(courses.get(courseIndex).getOtherEnlistedStudents().get(otherIndex).getFirstName());
                    stringBuilder.append(" ");
                    stringBuilder.append(courses.get(courseIndex).getOtherEnlistedStudents().get(otherIndex).getID());
                    if (otherIndex != courses.get(courseIndex).getOtherEnlistedStudents().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                // end of info 3
                stringBuilder.append(";");

                // writhe the teacher for the course
                for (int teacherIndex = 0; teacherIndex < courses.get(courseIndex).getTeachers().size(); teacherIndex++) {
                    stringBuilder.append(courses.get(courseIndex).getTeachers().get(teacherIndex).getFirstName());
                    stringBuilder.append(" ");
                    stringBuilder.append(courses.get(courseIndex).getTeachers().get(teacherIndex).getID());
                    if (teacherIndex != courses.get(courseIndex).getTeachers().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                // end of info 4
                stringBuilder.append(";");

                // write the model for the goals for the course
                CourseGoalModel courseGoalModel = courses.get(courseIndex).getCourseGoalModel();
                // goals
                for (int rowGoal = 0; rowGoal < courseGoalModel.getObjective().size(); rowGoal++) {
                    stringBuilder.append(courseGoalModel.getObjective().get(rowGoal));
                    if (rowGoal != courseGoalModel.getObjective().size() - 1) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append(":");
                // partGoals
                for (int colGoal = 0; colGoal < courseGoalModel.getMilestone().size(); colGoal++) {
                    stringBuilder.append(courseGoalModel.getMilestone().get(colGoal));
                    if (colGoal != courseGoalModel.getMilestone().size() - 1) {
                        stringBuilder.append(",");
                    }
                }

                stringBuilder.append(":");
                // write the maxPoints for each milestone
                for (int maxRow = 0; maxRow < courseGoalModel.getMaxPoits().size(); maxRow++) {
                    for (int maxCol = 0; maxCol < courseGoalModel.getMaxPoits().get(0).size(); maxCol++) {
                        stringBuilder.append(courseGoalModel.getMaxPoits().get(maxRow).get(maxCol));
                        if (maxRow == courseGoalModel.getMaxPoits().size()-1 && maxCol == courseGoalModel.getMaxPoits().get(0).size()-1) {
                            continue;
                        }else {
                            stringBuilder.append(",");
                        }
                    }
                }
                // end of info 5
                stringBuilder.append(";");

                // add all the removed students from the groups
                // adding all the persons ID with "," as separator between persons
                for (int removedIndex = 0; removedIndex < courses.get(courseIndex).getListOfRemovedGroupStudents().size(); removedIndex++) {
                    stringBuilder.append(courses.get(courseIndex).getListOfRemovedGroupStudents().get(removedIndex));
                    if (removedIndex != courses.get(courseIndex).getListOfRemovedGroupStudents().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                // random solution on a random problem when the split function randomly started so malfunction in loadCourseFromString()
                stringBuilder.append(",");
                // end of info 6
                stringBuilder.append(";");

                // add all the gradeLevels for each objective
                for (int gradeRow = 0; gradeRow < courses.get(courseIndex).getCourseGradeModel().getGradeLevels().size(); gradeRow++) {
                    for (int gradeCol = 0; gradeCol < courses.get(courseIndex).getCourseGradeModel().getGradeLevels().get(gradeRow).size(); gradeCol++) {
                        stringBuilder.append(courses.get(courseIndex).getCourseGradeModel().getGradeLevels().get(gradeRow).get(gradeCol));
                        if (gradeCol != courses.get(courseIndex).getCourseGradeModel().getGradeLevels().get(gradeRow).size()-1) {
                            stringBuilder.append(",");
                        }
                    }
                    if (gradeRow != courses.get(courseIndex).getCourseGradeModel().getGradeLevels().size()-1) {
                        stringBuilder.append(":");
                    }
                }
                // end of info 7
                stringBuilder.append(";");

                fileWriter.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
            }
            fileWriter.write("<".getBytes());

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("content/Settings.txt"));
            StringBuilder stringBuilder = new StringBuilder();

            // appending the last id Used
            stringBuilder.append(settingsLoader.getLastIDNumber());

            //appending all the scatterd numbers of removed persons
            stringBuilder.append(":");
            for (int scatterIndex = 0; scatterIndex < settingsLoader.getScatteredIDNumbers().size(); scatterIndex++) {
                stringBuilder.append(settingsLoader.getScatteredIDNumbers().get(scatterIndex));
                if (scatterIndex != settingsLoader.getScatteredIDNumbers().size()-1) {
                    stringBuilder.append(",");
                }
            }

            stringBuilder.append(";");
            fileWriter.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
