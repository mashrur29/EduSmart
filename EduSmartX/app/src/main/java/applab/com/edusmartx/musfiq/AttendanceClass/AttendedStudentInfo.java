package applab.com.edusmartx.musfiq.AttendanceClass;

public class AttendedStudentInfo {


    public String profileLink,studentName,studentRoll;


    public AttendedStudentInfo() {
    }

    public AttendedStudentInfo(String profileLink, String studentName, String studentRoll) {
        this.profileLink = profileLink;
        this.studentName = studentName;
        this.studentRoll = studentRoll;
    }


    public String getProfileLink() {
        return profileLink;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }
}
