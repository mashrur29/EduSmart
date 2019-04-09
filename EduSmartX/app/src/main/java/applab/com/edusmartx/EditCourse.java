package applab.com.edusmartx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;


public class EditCourse extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Firebase.setAndroidContext(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addDoneButt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String course,instructor;
                course = ((EditText) findViewById(R.id.course_name)).getText().toString();
                instructor = ((EditText) findViewById(R.id.inst_name)).getText().toString();


                Firebase FDataBaseRef = new Firebase("https://edusmart-8a0e7.firebaseio.com/");
                Firebase courseRef = FDataBaseRef.child("Courses");

                Firebase curCourse= courseRef.child(course);
                curCourse.child("coursename").setValue(course);
                curCourse.child("instructor").setValue(instructor);



                Intent resultIntent = new Intent();
                resultIntent.putExtra("course", course);
                resultIntent.putExtra("instructor", instructor);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }





}
