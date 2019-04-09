package applab.com.edusmartx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CourseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

    }



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
