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

public class EditCourse extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addDoneButt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String course,instructor;
                course = ((EditText) findViewById(R.id.course_name)).getText().toString();
                instructor = ((EditText) findViewById(R.id.inst_name)).getText().toString();


                Intent resultIntent = new Intent();
                resultIntent.putExtra("course", course);
                resultIntent.putExtra("instructor", instructor);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }





}
