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

import applab.com.edusmartx.DataBase.ProfileActivity;
import applab.com.edusmartx.R;

public class LogInActivity extends AppCompatActivity {

    EditText email, pass;
    Button login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        login = findViewById(R.id.loginBtn);

        auth = FirebaseAuth.getInstance();
    }

    public void LogInUser(View v) {

//        if (email.getText().toString().equals("") || pass.getText().toString().equals("")) return;

//        String password = "zzzzzzzq";
//        String email = password+"01@mail.com";

//        auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            auth.signInWithEmailAndPassword("zzzzzzzz01@mail.com","zzzzzzzz").addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                    finish();
                    startActivity(profileIntent);
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
