package main.DataStore;

import main.DataStore.Lexicon.PersonLexicon;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

    public DataSaver(PersonLexicon personLexicon, YearHolderPage yearHolderPage, CoursesPage coursesPage) {
        this.personLexicon = personLexicon;
        this.yearHolderPage = yearHolderPage;
        this.coursesPage = coursesPage;
    }

    @Override
    public void run() {
        System.out.println("started saving");

        try {
            // saving the persons and grade for the students
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("content/Students.txt"));
            System.out.println("found students.txt");
            personLexicon.saveLexicon(fileWriter);
            fileWriter.flush();
            fileWriter.close();
            System.out.println("done with personLexicon");
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
                stringBuilder.append(courses.get(courseIndex).getClassInfo().getClassName());
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
                for (int rowGoal = 0; rowGoal < courseGoalModel.getGoals().size(); rowGoal++) {
                    stringBuilder.append(courseGoalModel.getGoals().get(rowGoal));
                    if (rowGoal != courseGoalModel.getGoals().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append(":");
                // partGoals
                for (int colGoal = 0; colGoal < courseGoalModel.getPartGoals().size(); colGoal++) {
                    stringBuilder.append(courseGoalModel.getPartGoals().get(colGoal));
                    if (colGoal != courseGoalModel.getPartGoals().size()-1) {
                        stringBuilder.append(",");
                    }
                }
                // end of info 5
                stringBuilder.append(";");



                fileWriter.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
            }
            fileWriter.write("<".getBytes());

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}