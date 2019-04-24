package applab.com.edusmartx.musfiq.Classroom;

public class AssignmentInfo {

    public String fileLink, fileName;

    public AssignmentInfo(String fileLink, String fileName) {
        this.fileLink = fileLink;
        this.fileName = fileName;
    }

    public AssignmentInfo() {
    }


    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
