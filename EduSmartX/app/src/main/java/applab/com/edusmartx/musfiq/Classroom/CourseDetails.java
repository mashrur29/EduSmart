package applab.com.edusmartx.musfiq.Classroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import applab.com.edusmartx.R;

public class CourseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_2);

    }
    ///



    public void assignmentClicked(View v){
        Intent intent = new Intent(CourseDetails.this, AssignmentSelection.class);
        intent.putExtra("currentCatagory", "assignment");
        startActivityForResult(intent, 500);
    }


    public void projectClicked(View v){
        Intent intent = new Intent(CourseDetails.this, AssignmentSelection.class);
        intent.putExtra("currentCatagory", "project");
        startActivityForResult(intent, 500);
    }



}
