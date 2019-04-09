package applab.com.edusmartx;

/**
 * Created by musfiq on 4/9/19.
 */

public class CourseInfo {


    public String coursename, instructor;


    public CourseInfo() {
    }

    public CourseInfo(String coursename, String instructor) {
        this.coursename = coursename;
        this.instructor = instructor;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}



