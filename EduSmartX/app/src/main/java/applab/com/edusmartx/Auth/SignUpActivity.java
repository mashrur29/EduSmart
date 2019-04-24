package applab.com.edusmartx.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import applab.com.edusmartx.musfiq.Classroom.CourseListActivity;
import applab.com.edusmartx.R;

public class SignUpActivity extends AppCompatActivity {

    EditText email,pass;
    Button signup;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        signup =  findViewById(R.id.signupBtn);

        auth = FirebaseAuth.getInstance();

    }

    public void createUser(View v)
    {

        if(email.getText().toString().equals("") || pass.getText().toString().equals(""))return;

//        String password = "zzzzzzzq";
//        String email = password+"01@mail.com";

        auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent profileIntent = new Intent(getApplicationContext(), CourseListActivity.class);
                    finish();
                    startActivity(profileIntent);
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}