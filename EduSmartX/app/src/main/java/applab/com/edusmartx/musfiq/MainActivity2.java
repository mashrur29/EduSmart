package applab.com.edusmartx.musfiq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import applab.com.edusmartx.R;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = new Intent(MainActivity2.this, CourseListActivity.class);
//        intent.putExtra("currentCatagory", currCatagory);
//        intent.putExtra("NID", "-1");
        startActivityForResult(intent, 500);


    }
}
