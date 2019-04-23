package applab.com.edusmartx.Auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import applab.com.edusmartx.musfiq.CourseListActivity;
import applab.com.edusmartx.R;
import applab.com.edusmartx.musfiq.homepage;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button signup,login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        signup = findViewById(R.id.signupBtn);
        login = findViewById(R.id.loginBtn);

//        startActivity(new Intent(this, CourseListActivity.class));
        startActivity(new Intent(this, homepage.class));
    }

    public void openActivityLogin(View v) {
        startActivity(new Intent(this, LogInActivity.class));
    }

    public void openActivitySignUp(View v) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
//        Intent intent = new Intent(MainActivity2.this, CourseListActivity.class);
////        intent.putExtra("currentCatagory", currCatagory);
////        intent.putExtra("NID", "-1");
//        startActivityForResult(intent, 500);


