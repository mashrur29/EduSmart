package applab.com.edusmartx.DataBase;

public class Course {
    private String coursename;
    private String instructor;

    public String getCoursename() {
        return coursename;
    }

    public String getInstructor() {
        return instructor;
    }

    public Course(String coursename, String instructor) {
        this.coursename = coursename;
        this.instructor = instructor;
    }
}
