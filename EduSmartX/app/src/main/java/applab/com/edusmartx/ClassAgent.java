package applab.com.edusmartx;

import java.util.ArrayList;

/**
 * Created by musfiq on 4/9/19.
 */

public class ClassAgent {


    public int classNo;
    public ArrayList<ClassroomInstance>domain;

    public ClassAgent(int classNo, ArrayList<ClassroomInstance> domain) {
        this.classNo = classNo;
        this.domain = domain;
    }
}
