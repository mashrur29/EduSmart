package applab.com.edusmartx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
//        intent.putExtra("currentCatagory", currCatagory);
//        intent.putExtra("NID", "-1");
        startActivityForResult(intent, 500);


    }
}
